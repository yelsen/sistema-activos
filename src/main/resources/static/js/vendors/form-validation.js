/**
 * =================================================================
 * Form Validation Utilities - V2.4 (Instant Feedback & Choices.js Fix)
 * =================================================================
 * Utilidades para validación de formularios con Bootstrap 5 en tiempo real.
 */

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

    // Listener para el evento de submit
    form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
            if (typeof showToast === 'function') {
                showToast('Por favor, corrija los errores en el formulario.', 'warning');
            }
        }
        // Bootstrap se encarga del resto al añadir 'was-validated'
        form.classList.add('was-validated');
    }, false);

    // Listener para validación en tiempo real en todos los campos
    form.addEventListener('input', function(event) {
        const field = event.target;
        if (field.matches('.form-control, .form-select')) {
            validateField(field); // Validate immediately on input
        }
    });

    // Listener específico para 'change' en selects y checkboxes
    form.addEventListener('change', function(event) {
        const field = event.target;
        if (field.matches('.form-select, .form-check-input')) { // Validate immediately on change
            validateField(field);
        }
    });
};

/**
 * Valida un campo individual y actualiza su estado visual.
 * @param {HTMLElement} field - El elemento del campo a validar.
 * @param {boolean} [force=false] - Si es true, fuerza la validación y muestra el feedback.
 * @returns {boolean} - `true` si el campo es válido, `false` en caso contrario.
 */
window.validateField = function (field) {
    if (!field) return true;

    // Limpia validaciones personalizadas previas
    field.setCustomValidity('');

    const isValid = field.checkValidity();

    // Determine if validation feedback should be shown (e.g., don't show green check on empty optional fields)
    // Show feedback if field has content, is required, or is invalid.
    const showFeedback = field.value.trim() !== '' || field.required || !isValid;

    if (isValid && showFeedback) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    } else {
        field.classList.remove('is-valid');
        if (!isValid) { // Only add is-invalid if it's actually invalid
            field.classList.add('is-invalid');
        } else { // If valid but no feedback (e.g., empty optional field), ensure no invalid class is present
            field.classList.remove('is-invalid');
        }
    }

    // Para selects con Choices.js, aplica la clase al contenedor
    if (field.tagName === 'SELECT' && field.closest('.choices')) {
        const choicesContainer = field.closest('.choices');
        choicesContainer.classList.remove('is-valid', 'is-invalid'); // Clear previous state
        if (isValid && field.value) {
            choicesContainer.classList.add('is-valid');
        } else {
            if (!isValid) {
                choicesContainer.classList.add('is-invalid');
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
window.setupPasswordValidation = function (config) {
    const passwordInput = document.getElementById(config.passwordId);
    const confirmInput = document.getElementById(config.confirmId);
    const strengthBar = document.getElementById(config.strengthBarId);
    const strengthText = document.getElementById(config.strengthTextId);
    const reqs = {
        length: document.getElementById(config.requirements.length),
        uppercase: document.getElementById(config.requirements.uppercase),
        lowercase: document.getElementById(config.requirements.lowercase),
        number: document.getElementById(config.requirements.number),
        match: document.getElementById(config.requirements.match)
    };

    if (!passwordInput || !confirmInput) return;

    const updateRequirement = (element, isValid) => {
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

    const checkPasswordMatch = () => {
        const password = passwordInput.value;
        const confirm = confirmInput.value;
        const doPasswordsMatch = password && confirm && password === confirm;

        updateRequirement(reqs.match, doPasswordsMatch);
        confirmInput.setCustomValidity(doPasswordsMatch || !confirm ? '' : 'Las contraseñas no coinciden.'); // Set custom validity
        validateField(confirmInput);
        return doPasswordsMatch;
    };

    const checkPasswordStrength = () => {
        const password = passwordInput.value;
        let strength = 0;
        const validations = {
            length: password.length >= 8,
            lowercase: /[a-z]/.test(password),
            uppercase: /[A-Z]/.test(password),
            number: /[0-9]/.test(password)
        };

        Object.values(validations).forEach(valid => { if (valid) strength += 25; });

        updateRequirement(reqs.length, validations.length);
        updateRequirement(reqs.lowercase, validations.lowercase);
        updateRequirement(reqs.uppercase, validations.uppercase);
        updateRequirement(reqs.number, validations.number);

        if (strengthBar) strengthBar.style.width = `${strength}%`;
        if (strengthBar) strengthBar.className = 'progress-bar';

        if (strength < 50) {
            if (strengthBar) strengthBar.classList.add('bg-danger');
            if (strengthText) { strengthText.className = 'badge bg-danger'; strengthText.textContent = 'Débil'; }
        } else if (strength < 100) {
            if (strengthBar) strengthBar.classList.add('bg-warning');
            if (strengthText) { strengthText.className = 'badge bg-warning'; strengthText.textContent = 'Media'; }
        } else {
            if (strengthBar) strengthBar.classList.add('bg-success');
            if (strengthText) { strengthText.className = 'badge bg-success'; strengthText.textContent = 'Fuerte'; }
        }

        const isPasswordValid = strength === 100;
        passwordInput.setCustomValidity(isPasswordValid || !password ? '' : 'La contraseña no cumple los requisitos.');
        validateField(passwordInput, true); // Force validation
        checkPasswordMatch();
    };

    passwordInput.addEventListener('input', checkPasswordStrength);
    confirmInput.addEventListener('input', checkPasswordMatch);
};

/**
 * Configura un input cuya validación (patrón, longitud) depende de un select.
 * @param {Object} config - Objeto de configuración.
 * @param {string} config.selectId - ID del select que controla la validación.
 * @param {string} config.inputId - ID del input a validar.
 * @param {string} [config.infoPanelId] - (Opcional) ID del panel para mostrar info.
 * @param {string} [config.invalidFeedbackId] - (Opcional) ID del div para el mensaje de error.
 * @param {Function} [config.onSelectChange] - (Opcional) Callback que se ejecuta al cambiar el select.
 */
window.setupDynamicInputValidation = function (config) {
    const selectEl = document.getElementById(config.selectId);
    const inputEl = document.getElementById(config.inputId);
    const infoPanel = config.infoPanelId ? document.getElementById(config.infoPanelId) : null;
    const invalidFeedback = config.invalidFeedbackId ? document.getElementById(config.invalidFeedbackId) : null;

    if (!selectEl || !inputEl) return;

    const updateValidation = () => {
        const selectedOption = selectEl.options[selectEl.selectedIndex];

        // Resetear estado del input
        inputEl.value = '';
        inputEl.pattern = '.*'; // Reset pattern
        inputEl.minLength = -1;
        inputEl.maxLength = 524288;
        inputEl.classList.remove('is-valid', 'is-invalid');
        if (infoPanel) infoPanel.classList.add('d-none');

        if (!selectedOption || !selectedOption.value) {
            inputEl.disabled = true;
            inputEl.placeholder = 'Seleccione una opción primero';
            if (invalidFeedback) invalidFeedback.textContent = 'Seleccione una opción primero.';
        } else {
            inputEl.disabled = false;
            const { patron, min, max, nombre } = selectedOption.dataset;

            if (patron && patron !== 'null') inputEl.pattern = patron;
            if (min) inputEl.minLength = min;
            if (max) inputEl.maxLength = max;

            inputEl.placeholder = `Ingrese ${nombre || 'el valor'}`;

            if (invalidFeedback) {
                const longitud = (min === max) ? `${min}` : `${min}-${max}`;
                invalidFeedback.textContent = `Ingrese un ${nombre} válido (${longitud} caracteres).`;
            }

            if (infoPanel) {
                infoPanel.querySelector('#documento-tipo-nombre').textContent = nombre;
                infoPanel.querySelector('#documento-longitud').textContent = (min === max) ? `${min}` : `${min}-${max}`;
                infoPanel.classList.remove('d-none');
            }
        }

        // Ejecutar callback si existe
        if (config.onSelectChange) {
            config.onSelectChange(selectedOption);
        }
        validateField(inputEl);
    };

    selectEl.addEventListener('change', updateValidation);
    inputEl.addEventListener('input', () => validateField(inputEl, true)); // Real-time validation for the input itself
    // Inicializar al cargar
    updateValidation();
};

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
        field.classList.remove('is-valid', 'is-invalid');
        // Para Choices.js
        if (field.tagName === 'SELECT' && field.closest('.choices')) {
            field.closest('.choices').classList.remove('is-valid', 'is-invalid');
        }
    });
};
