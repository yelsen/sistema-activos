/**
 * Dynamic Filters - Sistema de filtros dinámicos con HTMX
 */

/**
 * Configurar filtros dinámicos para una página
 * @param {string} formId - ID del formulario de filtros
 * @param {string} containerId - ID del contenedor que se actualiza
 * @param {string} baseUrl - URL base para las peticiones
 * @param {number} debounceMs - Tiempo de debounce en milisegundos
 */
window.setupDynamicFilters = function(formId, containerId, baseUrl, debounceMs = 300) {
    const filterForm = document.getElementById(formId);
    const container = document.getElementById(containerId);
    
    if (!filterForm || !container) {
        console.warn(`No se encontró el formulario ${formId} o el contenedor ${containerId}`);
        return;
    }

    let debounceTimer;

    /**
     * Función para realizar la petición de filtros
     */
    function fetchFilteredData() {
        const formData = new FormData(filterForm);
        const params = new URLSearchParams(formData);
        const url = `${baseUrl}?${params.toString()}`;

        // Actualizar URL del navegador
        history.pushState(null, '', url);

        // Usar la función helper de HTMX
        if (typeof htmxFetch === 'function') {
            htmxFetch(url, 'GET', { 'HX-Target': containerId })
                .then(response => response.text())
                .then(html => {
                    if (container) {
                        // Destruir Choices.js antes de actualizar
                        if (typeof destroyChoicesInstances === 'function') {
                            destroyChoicesInstances();
                        }
                        
                        container.innerHTML = html;
                        
                        // Reinicializar Choices.js después de actualizar
                        setTimeout(() => {
                            if (typeof initializeChoicesSafe === 'function') {
                                initializeChoicesSafe();
                            }
                        }, 100);
                    }
                })
                .catch(error => {
                    console.error('Error al filtrar datos:', error);
                    if (typeof showToast === 'function') {
                        showToast('Error al filtrar. Intente nuevamente.', 'error');
                    }
                });
        }
    }

    // Event listeners para el formulario
    if (filterForm) {
        // Búsqueda con debounce para inputs de texto
        filterForm.addEventListener('input', (e) => {
            if (e.target.type === 'text' || e.target.type === 'search') {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(fetchFilteredData, debounceMs);
            }
        });

        // Búsqueda inmediata para selects y otros elementos
        filterForm.addEventListener('change', (e) => {
            if (e.target.tagName === 'SELECT' || e.target.type === 'radio' || e.target.type === 'checkbox') {
                fetchFilteredData();
            }
        });
    }

    // Retornar función para uso manual si es necesario
    return {
        refresh: fetchFilteredData,
        destroy: () => {
            clearTimeout(debounceTimer);
        }
    };
};

/**
 * Configurar filtros específicos para roles
 * @param {Object} options - Opciones de configuración
 */
window.setupRolesFilters = function(options = {}) {
    const defaults = {
        formId: 'filterForm',
        containerId: 'roles-list-container',
        baseUrl: '/seguridad/roles',
        debounceMs: 300
    };

    const config = { ...defaults, ...options };
    return setupDynamicFilters(config.formId, config.containerId, config.baseUrl, config.debounceMs);
};
