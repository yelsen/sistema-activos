/**
 * Aplica validación en tiempo real a un formulario.
 * @param {string} formId - El ID del formulario a validar.
 */
window.setupRealTimeValidation = function (formId) {
    const form = document.getElementById(formId);
    if (!form) {
        console.warn(`Formulario con ID "${formId}" no encontrado.`);
        return;
    }

    // Evitar doble binding si ya se configuró
    if (form.dataset.validationBound === 'true') {
        return;
    }
    form.dataset.validationBound = 'true';

    form.querySelectorAll('select[data-choices]').forEach(function (sel) {
        let choicesContainer = sel.closest('.choices');
        if (!choicesContainer) {
            const nextEl = sel.nextElementSibling;
            const prevEl = sel.previousElementSibling;
            if (nextEl && nextEl.classList && nextEl.classList.contains('choices')) choicesContainer = nextEl;
            else if (prevEl && prevEl.classList && prevEl.classList.contains('choices')) choicesContainer = prevEl;
        }
        sel.classList.remove('is-valid', 'is-invalid');
        if (choicesContainer) {
            choicesContainer.classList.remove('is-valid', 'is-invalid');
        }
    });

    // Detectar automáticamente campos de contraseña y configurar validación
    autoSetupPasswordValidation(form);

    // Listener para el evento de submit
    form.addEventListener('submit', function (event) {
        const isFormValid = form.checkValidity();
        if (!isFormValid) {
            event.preventDefault();
            event.stopPropagation();
            if (typeof showToast === 'function') {
                showToast('Por favor, corrija los errores en el formulario.', 'warning');
            }
        }
        // Añadir estado global de validación
        form.classList.add('was-validated');
        // Sincronizar estado visual de selects con Choices (requeridos vacíos -> inválidos)
        form.querySelectorAll('select[data-choices]').forEach(function (sel) {
            validateChoicesSelect(sel);
        });
    }, false);

    // Capturar eventos de invalidación a nivel de formulario (no hacen bubble)
    form.addEventListener('invalid', function (event) {
        const field = event.target;
        // Importante: NO llamar a validateField/validateChoicesSelect aquí para evitar bucles
        if (field.matches('select[data-choices]')) {
            let choicesContainer = field.closest('.choices');
            if (!choicesContainer) {
                const nextEl = field.nextElementSibling;
                const prevEl = field.previousElementSibling;
                if (nextEl && nextEl.classList && nextEl.classList.contains('choices')) choicesContainer = nextEl;
                else if (prevEl && prevEl.classList && prevEl.classList.contains('choices')) choicesContainer = prevEl;
            }
            if (choicesContainer) {
                choicesContainer.classList.remove('is-valid');
                choicesContainer.classList.add('is-invalid');
            }
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');
        } else if (field.matches('.form-control, .form-select')) {
            field.classList.remove('is-valid');
            field.classList.add('is-invalid');
        }
    }, true);

    // Listener para validación en tiempo real en todos los campos
    form.addEventListener('input', function (event) {
        const field = event.target;
        if (field.matches('.form-control, .form-select')) {
            validateField(field); // Validate immediately on input
        }
    });

    // Listener específico para 'change' en selects y checkboxes
    form.addEventListener('change', function (event) {
        const field = event.target;
        if (field.matches('.form-select, .form-check-input')) { // Validate immediately on change
            validateField(field);
            if (field.matches('select[data-choices]')) {
                validateChoicesSelect(field);
            }
        }
    });
};

/**
 * Valida un campo individual y actualiza su estado visual.
 * @param {HTMLElement} field - El elemento del campo a validar.
 * @param {boolean} [force=false] - Si es true, fuerza la validación y muestra el feedback.
 * @returns {boolean} - `true` si el campo es válido, `false` en caso contrario.
 */
// Ajuste: omitir validación visual inline en campos con data-no-inline-validation
window.validateField = function (field) {
    if (!field) return true;

    // Si el campo indica que no desea validación visual inline, solo devolver su validez
    if (field.dataset && field.dataset.noInlineValidation === 'true') {
        return field.validity ? field.validity.valid : true;
    }

    // Limpia validaciones personalizadas previas
    field.setCustomValidity('');

    // Usar API de Constraint Validation sin disparar eventos
    const isValid = field.validity ? field.validity.valid : true;
    const showFeedback = (typeof field.value === 'string' ? field.value.trim() !== '' : !!field.value) || field.required || !isValid;

    if (isValid && showFeedback) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    } else {
        field.classList.remove('is-valid');
        if (!isValid) {
            field.classList.add('is-invalid');
        } else {
            field.classList.remove('is-invalid');
        }
    }

    if (field.tagName === 'SELECT') {
        let choicesContainer = field.closest('.choices');
        if (!choicesContainer) {
            const nextEl = field.nextElementSibling;
            const prevEl = field.previousElementSibling;
            if (nextEl && nextEl.classList && nextEl.classList.contains('choices')) choicesContainer = nextEl;
            else if (prevEl && prevEl.classList && prevEl.classList.contains('choices')) choicesContainer = prevEl;
        }
        if (choicesContainer) {
            choicesContainer.classList.remove('is-valid', 'is-invalid');
            if (isValid && field.value) {
                choicesContainer.classList.add('is-valid');
            } else {
                if (!isValid) {
                    choicesContainer.classList.add('is-invalid');
                }
            }
        }
    }

    return isValid;
};

/**
 * Configura la validación de fortaleza y coincidencia de contraseñas.
 * @param {Object} config - Objeto de configuración.
 * @param {string} config.passwordId - ID del input de contraseña.
 * @param {string} config.confirmId - ID del input de confirmación de contraseña.
 * @param {string} config.strengthBarId - ID de la barra de progreso de fortaleza.
 * @param {string} config.strengthTextId - ID del span para el texto de fortaleza.
 * @param {Object} config.requirements - IDs para los elementos de la lista de requisitos.
 */
// setupPasswordValidation eliminado por no usarse; usar autoSetupPasswordValidation + setupPasswordValidationWithPanel.


/**
 * Limpia el estado de validación de un formulario.
 * @param {string} formId - ID del formulario.
 */
window.clearFormValidation = function (formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    // Quita la clase principal de validación del formulario
    form.classList.remove('was-validated');

    form.querySelectorAll('.is-valid, .is-invalid').forEach(field => {
        // Limpiar mensajes de validez personalizada
        if (typeof field.setCustomValidity === 'function') {
            field.setCustomValidity('');
        }
        field.classList.remove('is-valid', 'is-invalid');
        // Para Choices.js (padre o hermano contenedor)
        if (field.tagName === 'SELECT') {
            let choicesContainer = field.closest('.choices');
            if (!choicesContainer) {
                const nextEl = field.nextElementSibling;
                const prevEl = field.previousElementSibling;
                if (nextEl && nextEl.classList && nextEl.classList.contains('choices')) choicesContainer = nextEl;
                else if (prevEl && prevEl.classList && prevEl.classList.contains('choices')) choicesContainer = prevEl;
            }
            if (choicesContainer) {
                choicesContainer.classList.remove('is-valid', 'is-invalid');
            }
        }
    });
};

// Validador específico para selects con data-choices
window.validateChoicesSelect = function (selectEl) {
    if (!selectEl || selectEl.tagName !== 'SELECT') return true;

    // Validez: usar API sin eventos + comprobar valor no vacío
    const isValid = (selectEl.validity ? selectEl.validity.valid : true) && (!!selectEl.value && selectEl.value !== '');

    // Buscar contenedor Choices (padre o hermanos)
    let choicesContainer = selectEl.closest('.choices');
    if (!choicesContainer) {
        const nextEl = selectEl.nextElementSibling;
        const prevEl = selectEl.previousElementSibling;
        if (nextEl && nextEl.classList && nextEl.classList.contains('choices')) choicesContainer = nextEl;
        else if (prevEl && prevEl.classList && prevEl.classList.contains('choices')) choicesContainer = prevEl;
    }

    if (choicesContainer) {
        choicesContainer.classList.remove('is-valid', 'is-invalid');
        if (isValid) {
            choicesContainer.classList.add('is-valid');
        } else {
            choicesContainer.classList.add('is-invalid');
        }
    }

    // No invocar validateField aquí para evitar bucles
    return isValid;
};

/**
 * Detecta automáticamente campos de contraseña en un formulario y configura la validación
 * @param {HTMLFormElement} form - El formulario a analizar
 */
window.autoSetupPasswordValidation = function (form) {
    const passwordInputs = form.querySelectorAll('input[type="password"]');
    if (passwordInputs.length === 0) return;

    let mainPasswordInput = null;
    let confirmPasswordInput = null;

    passwordInputs.forEach(input => {
        const id = input.id.toLowerCase();
        const name = (input.name || '').toLowerCase();
        if (id.includes('confirmar') || id.includes('confirm') || name.includes('confirmar') || name.includes('confirm')) {
            confirmPasswordInput = input;
        } else {
            mainPasswordInput = input;
        }
    });

    if (!mainPasswordInput && passwordInputs.length > 0) {
        mainPasswordInput = passwordInputs[0];
    }
    if (!confirmPasswordInput && passwordInputs.length > 1) {
        confirmPasswordInput = passwordInputs[1];
    }
    if (!mainPasswordInput) return;

    const lastPasswordInput = confirmPasswordInput || mainPasswordInput;

    // 1) Reutilizar panel existente si ya está presente en el formulario
    let strengthPanel = null;
    const existingStrengthBar = form.querySelector('.strength-bar');
    if (existingStrengthBar) {
        strengthPanel = existingStrengthBar.closest('.card-body') || existingStrengthBar.closest('.col-12') || existingStrengthBar.parentElement;
    }

    // 2) Si no existe, crear uno nuevo e insertarlo después del último campo
    if (!strengthPanel) {
        strengthPanel = createPasswordStrengthPanel();
        const insertAfter = lastPasswordInput.closest('.col-md-6, .col-12, .mb-3') || lastPasswordInput.parentElement;
        if (insertAfter && insertAfter.parentElement) {
            insertAfter.parentElement.insertBefore(strengthPanel, insertAfter.nextSibling);
        }
        strengthPanel.id = `password-panel-${Date.now()}`;
    }

    setupPasswordValidationWithPanel(mainPasswordInput, confirmPasswordInput, strengthPanel);
};

/**
 * Crea el HTML del panel de fortaleza de contraseña
 * @returns {HTMLElement} - El elemento del panel creado
 */
window.createPasswordStrengthPanel = function () {
    const panelHTML = `
        <div class="col-12 mb-3">
            <div class="card border">
                <div class="card-body py-3 px-3">
                    <div class="row">
                        <div class="col-md-12 mb-2">
                            <div class="d-flex align-items-center justify-content-between mb-2">
                                <small class="text-muted fw-semibold">
                                    Fortaleza de contraseña
                                </small>
                                <span class="strength-text badge bg-secondary">Débil</span>
                            </div>
                            <div class="progress" style="height: 6px;">
                                <div class="progress-bar strength-bar" role="progressbar" style="width: 0%;"></div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <small class="text-muted d-block mb-2 fw-semibold">Requisitos mínimos:</small>
                            <ul class="list-unstyled small mb-0 password-requirements">
                                <li class="req-length mb-1 text-danger">
                                    <i class="ti ti-x text-danger me-1"></i> Mínimo 10 caracteres
                                </li>
                                <li class="req-uppercase mb-1 text-danger">
                                    <i class="ti ti-x text-danger me-1"></i> Al menos una mayúscula
                                </li>
                                <li class="req-lowercase mb-1 text-danger">
                                    <i class="ti ti-x text-danger me-1"></i> Al menos una minúscula
                                </li>
                                <li class="req-number mb-1 text-danger">
                                    <i class="ti ti-x text-danger me-1"></i> Al menos un número
                                </li>
                                <li class="req-special mb-1 text-danger">
                                    <i class="ti ti-x text-danger me-1"></i> Al menos un símbolo (!@#$%...)
                                </li>
                            </ul>
                        </div>
                        <div class="col-md-6">
                            <small class="text-muted d-block mb-2 fw-semibold">Verificación:</small>
                            <ul class="list-unstyled small mb-0">
                                <li class="req-match mb-1 text-danger">
                                    <i class="ti ti-x text-danger me-1"></i> Las contraseñas coinciden
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = panelHTML;
    return tempDiv.firstElementChild;
};

/**
 * Configura la validación de contraseña usando el panel creado dinámicamente
 * @param {HTMLInputElement} passwordInput - Campo de contraseña principal
 * @param {HTMLInputElement} confirmInput - Campo de confirmación (opcional)
 * @param {HTMLElement} panel - Panel de fortaleza creado
 */
window.setupPasswordValidationWithPanel = function (passwordInput, confirmInput, panel) {
    if (!passwordInput || !panel) return;

    const strengthBar = panel.querySelector('.strength-bar');
    const strengthText = panel.querySelector('.strength-text');
    const reqs = {
        length: panel.querySelector('.req-length'),
        uppercase: panel.querySelector('.req-uppercase'),
        lowercase: panel.querySelector('.req-lowercase'),
        number: panel.querySelector('.req-number'),
        special: panel.querySelector('.req-special'),
        match: panel.querySelector('.req-match')
    };

    passwordInput.dataset.noInlineValidation = 'true';
    if (confirmInput) {
        confirmInput.dataset.noInlineValidation = 'true';
    }

    const setNeutral = (el) => { el.classList.remove('is-valid', 'is-invalid'); };
    const setReq = (element, isValid) => {
        if (!element) return;
        const icon = element.querySelector('i');
        element.classList.toggle('text-success', isValid);
        element.classList.toggle('text-danger', !isValid);
        if (icon) {
            icon.classList.toggle('ti-check', isValid);
            icon.classList.toggle('ti-x', !isValid);
            icon.classList.toggle('text-success', isValid);
            icon.classList.toggle('text-danger', !isValid);
        }
    };

    const updateStrengthUI = (strength, allSatisfied) => {
        const pct = Math.max(0, Math.min(100, strength));
        if (strengthBar) {
            strengthBar.style.transition = 'width .25s ease, box-shadow .25s ease, background .25s ease';
            strengthBar.style.width = `${pct}%`;
        }
        const strongEnough = allSatisfied && pct >= 85;
        const mediumEnough = pct >= 50 && !strongEnough;
        if (!mediumEnough && !strongEnough) {
            if (strengthBar) {
                strengthBar.className = 'progress-bar bg-danger';
                strengthBar.style.background = 'linear-gradient(90deg,#ff4d4f,#ff7875)';
                strengthBar.style.boxShadow = '0 0 0.25rem rgba(255,77,79,.35)';
            }
            if (strengthText) { strengthText.className = 'badge bg-danger'; strengthText.textContent = 'Débil'; }
        } else if (mediumEnough) {
            if (strengthBar) {
                strengthBar.className = 'progress-bar bg-warning';
                strengthBar.style.background = 'linear-gradient(90deg,#faad14,#ffd666)';
                strengthBar.style.boxShadow = '0 0 0.25rem rgba(250,173,20,.35)';
            }
            if (strengthText) { strengthText.className = 'badge bg-warning'; strengthText.textContent = 'Media'; }
        } else {
            if (strengthBar) {
                strengthBar.className = 'progress-bar bg-success';
                strengthBar.style.background = 'linear-gradient(90deg,#52c41a,#73d13d)';
                strengthBar.style.boxShadow = '0 0 0.25rem rgba(82,196,26,.35)';
            }
            if (strengthText) { strengthText.className = 'badge bg-success'; strengthText.textContent = 'Fuerte'; }
        }
    };

    const checkPasswordMatch = () => {
        if (!confirmInput) return true;
        const password = passwordInput.value;
        const confirm = confirmInput.value;
        const matches = password && confirm && password === confirm;
        setReq(reqs.match, matches);
        confirmInput.setCustomValidity(matches || !confirm ? '' : 'Las contraseñas no coinciden.');
        setNeutral(confirmInput);
        return matches;
    };

    const checkPasswordStrength = () => {
        const password = passwordInput.value || '';
        const validations = {
            length: password.length >= 10,
            lowercase: /[a-z]/.test(password),
            uppercase: /[A-Z]/.test(password),
            number: /[0-9]/.test(password),
            special: /[^A-Za-z0-9]/.test(password)
        };

        let strength = 0;
        strength += validations.length ? 25 : 0;
        strength += validations.lowercase ? 15 : 0;
        strength += validations.uppercase ? 15 : 0;
        strength += validations.number ? 20 : 0;
        strength += validations.special ? 25 : 0;
        if (password.length >= 12) strength += 5;
        if (password.length >= 16) strength += 5;
        strength = Math.min(100, strength);

        setReq(reqs.length, validations.length);
        setReq(reqs.lowercase, validations.lowercase);
        setReq(reqs.uppercase, validations.uppercase);
        setReq(reqs.number, validations.number);
        setReq(reqs.special, validations.special);

        const allSatisfied = validations.length && validations.lowercase && validations.uppercase && validations.number && validations.special;
        updateStrengthUI(strength, allSatisfied);
        setNeutral(passwordInput);
        checkPasswordMatch();
    };

    passwordInput.addEventListener('input', checkPasswordStrength);
    if (confirmInput) {
        confirmInput.addEventListener('input', checkPasswordMatch);
    }

    const showPanel = () => { panel.style.display = 'block'; };
    passwordInput.addEventListener('focus', showPanel);
    passwordInput.addEventListener('input', showPanel);
};
