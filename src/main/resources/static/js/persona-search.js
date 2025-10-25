(function (window, document) {
    // Utilidad global reutilizable para búsqueda de personas por DNI
    // Uso básico:
    // PersonaSearch.init({
    //   modalId: 'modalCrearUsuario',
    //   formId: 'formCrearUsuario',
    //   dniFieldId: 'dniPersona',
    //   feedbackId: 'persona-feedback', // Opcional, por defecto 'persona-feedback'
    //   searchUrl: '/personas/buscar-usuario', // Opcional, se puede usar data-search-url en el modal
    //   // Los campos a rellenar se definen en el HTML con `data-search-field="nombreDelDato"`
    //   checkField: 'tieneUsuario', // Dato en la respuesta JSON que indica si la persona ya está asignada
    //   checkFieldLabel: 'Usuario',
    //   saveButtonId: 'btnGuardarUsuario',
    //   onPersonaFound: (data, helpers) => {},
    //   onPersonaNotFound: (helpers) => {},
    //   onPersonaYaAsignada: (data, helpers) => {},
    //   onError: (error, helpers) => {}
    // });

    const DEFAULT_DEBOUNCE_MS = 350;

    function renderFeedback(container, type, title, message) {
        const iconMap = {
            success: 'ti-circle-check',
            danger: 'ti-alert-circle',
            warning: 'ti-alert-triangle',
            info: 'ti-info-circle',
            primary: 'ti-search',
            secondary: 'ti-info-circle',
            light: 'ti-info-circle',
            dark: 'ti-info-circle'
        };
        const color = type || 'light';
        const icon = iconMap[color] || iconMap.info;
        if (!container) return;
        container.innerHTML = `
            <div class="alert alert-${color} alert-dismissible fade show d-flex align-items-start py-2 px-3 mb-0 mt-2" role="alert">
              <i class="ti ${icon} fs-5 me-2 flex-shrink-0"></i>
              <div class="flex-grow-1">
                <small class="fw-semibold d-block">${title || ''}</small>
                <small class="mb-0 d-block">${message || ''}</small>
              </div>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Cerrar"></button>
            </div>`;
    }

    function makeHelpers(opts) {
        const formEl = opts.formEl;
        const feedbackEl = opts.feedbackEl;
        const saveBtn = opts.saveBtn;

        const fieldsMap = opts.fieldsMap || {};

        function setFieldsDisabled(disabled) {
            Object.values(fieldsMap).forEach((fieldId) => {
                const field = document.getElementById(fieldId);
                if (field && field.id !== 'genero') {
                    field.disabled = !!disabled;
                    field.classList.toggle('bg-light', !!disabled);
                }
            });
            const generoId = fieldsMap['genero'];
            const genero = generoId ? document.getElementById(generoId) : null;
            const generoChoices = genero && genero.choices;
            if (generoChoices) { // Manejo especial para Choices.js
                disabled ? generoChoices.disable() : generoChoices.enable();
            } else if (genero) {
                genero.disabled = !!disabled;
            }
        }

        function applyData(data) {
            if (!data) return;
            Object.entries(fieldsMap).forEach(([respKey, fieldId]) => {
                const field = document.getElementById(fieldId);
                const value = data[respKey];
                if (field == null) return;

                if (fieldId === 'genero' && field.choices) {
                    field.choices.setChoiceByValue(value);
                } else {
                    field.value = value;
                }
            });
        }

        function clearFields(keysOrIds) {
            (keysOrIds || Object.keys(fieldsMap)).forEach((keyOrId) => {
                const id = fieldsMap[keyOrId] || keyOrId;
                const field = document.getElementById(id);
                if (!field) return;
                if (id === 'genero' && field.choices) {
                    field.choices.setChoiceByValue('');
                } else {
                    field.value = '';
                }
            });
        }

        function resetFormState() {
            if (formEl) {
                formEl.reset();
                formEl.classList.remove('was-validated');
                formEl.querySelectorAll('.is-invalid').forEach((f) => f.classList.remove('is-invalid'));
            }
            if (typeof window.clearFormValidation === 'function') {
                window.clearFormValidation(opts.formId);
            }
            renderFeedback(feedbackEl, 'light', 'Búsqueda de Persona', 'Ingrese un DNI de 8 dígitos.');
        }

        function renderFeedbackWrapper(type, title, message) {
            renderFeedback(feedbackEl, type, title, message);
        }

        function setSaveEnabled(enabled) {
            if (saveBtn) saveBtn.disabled = !enabled;
        }

        return {
            applyData,
            clearFields,
            setFieldsDisabled,
            resetFormState,
            renderFeedback: renderFeedbackWrapper,
            setSaveEnabled
        };
    }

    function init(config) {
        const {
            modalId,
            formId,
            dniFieldId = 'dniPersona',
            feedbackId = 'persona-feedback',
            searchUrl,
            checkField, // ej: 'tieneUsuario' | 'esResponsable' | 'esTecnico'
            checkFieldLabel, // ej: 'Usuario' | 'Responsable' | 'Técnico'
            saveButtonId,
            debounceMs = DEFAULT_DEBOUNCE_MS,
            onPersonaFound,
            onPersonaNotFound,
            onPersonaYaAsignada,
            onError
        } = config || {};

        const modalEl = document.getElementById(modalId);
        if (!modalEl) return;

        const effectiveUrl = searchUrl || modalEl.dataset.searchUrl;
        if (!effectiveUrl) {
            console.error(`[PersonaSearch] No se proporcionó 'searchUrl' ni se encontró 'data-search-url' en el modal "${modalId}". La inicialización ha sido cancelada.`);
            return;
        }

        const formEl = document.getElementById(formId);
        const dniInput = document.getElementById(dniFieldId);
        const feedbackEl = document.getElementById(feedbackId);
        const saveBtn = saveButtonId ? document.getElementById(saveButtonId) : null;

        // Construir el mapa de campos dinámicamente desde el formulario
        const fieldsMap = {};
        if (formEl) {
            formEl.querySelectorAll('[data-search-field]').forEach(field => {
                const key = field.dataset.searchField;
                if (key && field.id) {
                    fieldsMap[key] = field.id;
                }
            });
        }

        const opts = { formEl, feedbackEl, saveBtn, fieldsMap, formId };
        const helpers = makeHelpers(opts);

        let debounceTimer;
        let controller;
        let lastDni = '';

        function doSearch(dni) {
            if (!/^\d{8}$/.test(dni)) {
                if (dni.length > 0) {
                    renderFeedback(feedbackEl, 'warning', 'DNI Incompleto', 'Ingrese los 8 dígitos del DNI.');
                } else {
                    renderFeedback(feedbackEl, 'light', 'Búsqueda de Persona', 'Ingrese un DNI de 8 dígitos.');
                }
                return;
            }
            if (dni === lastDni) return;
            lastDni = dni;

            renderFeedback(feedbackEl, 'primary', 'Buscando...', `Consultando DNI ${dni}.`);

            try {
                if (controller) controller.abort();
                controller = new AbortController();
                const url = `${effectiveUrl}?dni=${encodeURIComponent(dni)}`;
                fetch(url, { signal: controller.signal })
                    .then((response) => {
                        if (response.ok) return response.json();
                        if (response.status === 404) return { notFound: true };
                        throw new Error(`Error del servidor: ${response.status}`);
                    })
                    .then((data) => {
                        if (data && (data.notFound || data.exists === false)) {
                            helpers.applyData(data);
                            helpers.setFieldsDisabled(false);
                            helpers.setSaveEnabled(true);
                            renderFeedback(feedbackEl, 'success', 'DNI Disponible', 'Complete los campos para registrar a la nueva persona.');
                            onPersonaNotFound && onPersonaNotFound(helpers);
                            return;
                        }

                        helpers.applyData(data);
                        const assigned = checkField ? !!data[checkField] : false;
                        if (assigned) {
                            helpers.setFieldsDisabled(true);
                            helpers.setSaveEnabled(false);
                            const label = checkFieldLabel || 'Asignación';
                            renderFeedback(feedbackEl, 'danger', `${label} existente`, 'Esta persona ya está asignada.');
                            onPersonaYaAsignada && onPersonaYaAsignada(data, helpers);
                        } else {
                            helpers.setFieldsDisabled(true);
                            helpers.setSaveEnabled(true);
                            renderFeedback(feedbackEl, 'info', 'Persona encontrada', 'Datos autocompletados, complete las credenciales.');
                            onPersonaFound && onPersonaFound(data, helpers);
                        }
                    })
                    .catch((err) => {
                        if (err?.name === 'AbortError') return;
                        helpers.setFieldsDisabled(false);
                        helpers.setSaveEnabled(true);
                        renderFeedback(feedbackEl, 'danger', 'Error de Conexión', 'No se pudo realizar la búsqueda.');
                        onError && onError(err, helpers);
                    });
            } catch (error) {
                if (error?.name === 'AbortError') return;
                helpers.setFieldsDisabled(false);
                helpers.setSaveEnabled(true);
                renderFeedback(feedbackEl, 'danger', 'Error de Conexión', 'No se pudo realizar la búsqueda.');
                onError && onError(error, helpers);
            }
        }

        if (dniInput) {
            dniInput.addEventListener('input', (e) => {
                const dni = e.target.value.trim();
                clearTimeout(debounceTimer);
                if (dni.length < 8) {
                    lastDni = '';
                    if (dni.length === 0) {
                        renderFeedback(feedbackEl, 'light', 'Búsqueda de Persona', 'Ingrese un DNI de 8 dígitos.');
                        helpers.setFieldsDisabled(false);
                        helpers.setSaveEnabled(true);
                    }
                }
                debounceTimer = setTimeout(() => doSearch(dni), debounceMs);
            });
        }

        modalEl.addEventListener('shown.bs.modal', () => {
            helpers.resetFormState();
            dniInput && dniInput.focus();
        });

        modalEl.addEventListener('hidden.bs.modal', () => {
            helpers.resetFormState();
            if (controller) controller.abort();
        });
    }

    window.PersonaSearch = { init };
})(window, document);
