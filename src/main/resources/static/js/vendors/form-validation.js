/**
 * Form Validation Utilities - Validación de formularios con Bootstrap
 */

/**
 * Configurar validación Bootstrap para un formulario (simplificada)
 * @param {string} formId - ID del formulario
 * @param {Object} customValidators - Validadores personalizados
 */
window.setupFormValidation = function(formId, customValidators = {}) {
    const form = document.getElementById(formId);
    if (!form) return;

    // Solo validación básica de HTML5, sin interferir con el servidor
    form.addEventListener('submit', function(event) {
        // Validación mínima: al menos un permiso seleccionado
        const checkboxes = form.querySelectorAll('input[name="permisosSeleccionados"]:checked');
        if (checkboxes.length === 0) {
            event.preventDefault();
            event.stopPropagation();
            
            if (typeof showToast === 'function') {
                showToast('Debe seleccionar al menos un permiso para el rol.', 'warning');
            }
            
            // Scroll hacia la tabla de permisos
            const permissionsTable = form.querySelector('.permissions-table-container');
            if (permissionsTable) {
                permissionsTable.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }
    });
};

/**
 * Validar un campo específico
 * @param {HTMLElement} field - Campo a validar
 * @param {Function} customValidator - Validador personalizado opcional
 */
function validateField(field, customValidator) {
    let isValid = field.checkValidity();
    let errorMessage = '';

    // Validador personalizado
    if (customValidator && isValid) {
        const customResult = customValidator(field.value);
        isValid = customResult.valid;
        errorMessage = customResult.message || '';
    }

    // Aplicar clases de Bootstrap
    if (isValid) {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    } else {
        field.classList.remove('is-valid');
        field.classList.add('is-invalid');
        
        // Mostrar mensaje personalizado si existe
        if (errorMessage) {
            const feedback = field.parentElement.querySelector('.invalid-feedback');
            if (feedback) {
                feedback.textContent = errorMessage;
            }
        }
    }

    return isValid;
}

/**
 * Validadores específicos para roles
 */
window.rolesValidators = {
    nombreRol: function(value) {
        if (value.length < 3) {
            return { valid: false, message: 'El nombre debe tener al menos 3 caracteres.' };
        }
        if (value.length > 45) {
            return { valid: false, message: 'El nombre no puede exceder 45 caracteres.' };
        }
        if (!/^[a-zA-ZÀ-ÿ\u00f1\u00d1\s]+$/.test(value)) {
            return { valid: false, message: 'El nombre solo puede contener letras y espacios.' };
        }
        return { valid: true };
    },

    descripcionRol: function(value) {
        if (value.length < 10) {
            return { valid: false, message: 'La descripción debe tener al menos 10 caracteres.' };
        }
        if (value.length > 255) {
            return { valid: false, message: 'La descripción no puede exceder 255 caracteres.' };
        }
        const words = value.trim().split(/\s+/);
        if (words.length < 3) {
            return { valid: false, message: 'La descripción debe tener al menos 3 palabras.' };
        }
        return { valid: true };
    },

    nivelAcceso: function(value) {
        const nivel = parseInt(value);
        if (!nivel || nivel < 1 || nivel > 5) {
            return { valid: false, message: 'Debe seleccionar un nivel entre 1 y 5.' };
        }
        return { valid: true };
    }
};

/**
 * Configurar validación específica para el formulario de crear rol
 */
window.setupRoleFormValidation = function() {
    setupFormValidation('formCrearRol', rolesValidators);
    
    // Validación adicional: al menos un permiso seleccionado
    const form = document.getElementById('formCrearRol');
    if (form) {
        form.addEventListener('submit', function(event) {
            const checkboxes = form.querySelectorAll('input[name="permisosSeleccionados"]:checked');
            if (checkboxes.length === 0) {
                event.preventDefault();
                event.stopPropagation();
                
                if (typeof showToast === 'function') {
                    showToast('Debe seleccionar al menos un permiso para el rol.', 'warning');
                }
                
                // Scroll hacia la tabla de permisos
                const permissionsTable = form.querySelector('.permissions-table-container');
                if (permissionsTable) {
                    permissionsTable.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }
        });
    }
};

/**
 * Limpiar validación de un formulario
 * @param {string} formId - ID del formulario
 */
window.clearFormValidation = function(formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.classList.remove('was-validated');
    const fields = form.querySelectorAll('.is-valid, .is-invalid');
    fields.forEach(field => {
        field.classList.remove('is-valid', 'is-invalid');
    });
};
