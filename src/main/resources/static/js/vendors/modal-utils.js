/**
 * Modal Utilities - Funciones reutilizables para manejo de modales
 */

/**
 * Inicializa la matriz de permisos en modales de roles
 * @param {HTMLElement} modalEl - Elemento del modal
 * @param {string} prefix - Prefijo para IDs únicos
 */
window.inicializarMatrizPermisos = function(modalEl, prefix) {
    if (!modalEl) return;

    const permissionsTable = modalEl.querySelector('.permissions-table');
    if (!permissionsTable) return;

    // Elementos de la interfaz
    const masterCheckbox = modalEl.querySelector(`#master-checkbox${prefix || ''}`);
    const allPermissionCheckboxes = permissionsTable.querySelectorAll('.permission-checkbox');
    const searchInput = modalEl.querySelector('.search-permissions');
    const filterButtons = modalEl.querySelector('.filter-buttons');
    const tableBody = permissionsTable.querySelector('tbody');

    // Contadores
    const totalModulesEl = modalEl.querySelector(`#totalModules${prefix || ''}`);
    const selectedPermissionsEl = modalEl.querySelector(`#selectedPermissions${prefix || ''}`);
    const totalPermissionsEl = modalEl.querySelector(`#totalPermissions${prefix || ''}`);

    // Actualización de contadores
    const updateStats = () => {
        if (!totalModulesEl || !selectedPermissionsEl || !totalPermissionsEl) return;
        totalModulesEl.textContent = tableBody.querySelectorAll('.module-row').length;
        selectedPermissionsEl.textContent = tableBody.querySelectorAll('.permission-checkbox:checked').length;
        totalPermissionsEl.textContent = allPermissionCheckboxes.length;
    };

    // Funcionalidad "Seleccionar Todos"
    if (masterCheckbox) {
        masterCheckbox.addEventListener('change', (e) => {
            const isChecked = e.target.checked;
            const visibleCheckboxes = tableBody.querySelectorAll('.module-row:not([style*="display: none"]) .permission-checkbox');
            visibleCheckboxes.forEach(checkbox => {
                checkbox.checked = isChecked;
            });
            updateStats();
        });
    }

    // Funcionalidad de Filtros
    if (filterButtons) {
        filterButtons.addEventListener('click', (e) => {
            const targetBtn = e.target.closest('.filter-btn');
            if (!targetBtn) return;

            filterButtons.querySelector('.active')?.classList.remove('active');
            targetBtn.classList.add('active');

            const filter = targetBtn.dataset.filter;

            tableBody.querySelectorAll('.module-row').forEach(row => {
                const hasChecked = row.querySelector('.permission-checkbox:checked');
                let show = true;
                if (filter === 'selected') show = hasChecked;
                else if (filter === 'unselected') show = !hasChecked;
                row.style.display = show ? '' : 'none';
            });
        });
    }

    // Búsqueda de Módulos
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            tableBody.querySelectorAll('.module-row').forEach(row => {
                const moduleName = row.dataset.moduleName.toLowerCase();
                row.style.display = moduleName.includes(query) ? '' : 'none';
            });
        });
    }

    // Actualizar estado al cambiar checkbox
    tableBody.addEventListener('change', (e) => {
        if (e.target.classList.contains('permission-checkbox')) {
            updateStats();
        }
    });

    updateStats();
};

/**
 * Configurar modal de edición con carga dinámica
 * @param {string} modalId - ID del modal
 * @param {string} contentId - ID del contenedor de contenido
 * @param {string} urlPattern - Patrón de URL para cargar contenido
 */
window.setupEditModal = function(modalId, contentId, urlPattern) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    const content = document.getElementById(contentId);
    if (!content) return;

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const itemId = button?.getAttribute('data-rol-id');

        if (!itemId) return;

        // Mostrar spinner
        content.innerHTML = `
            <div class="d-flex justify-content-center align-items-center p-5">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Cargando...</span>
                </div>
            </div>`;

        // Cargar contenido
        fetch(urlPattern.replace('{id}', itemId))
            .then(response => {
                if (!response.ok) throw new Error('Error al cargar datos');
                return response.text();
            })
            .then(html => {
                content.innerHTML = html;
                setTimeout(() => {
                    if (typeof inicializarMatrizPermisos === 'function') {
                        inicializarMatrizPermisos(modal, itemId);
                    }
                }, 100);
            })
            .catch(error => {
                console.error('Error:', error);
                content.innerHTML = `
                    <div class="alert alert-danger m-3">
                        <i class="ti ti-alert-circle me-2"></i>
                        No se pudo cargar la información.
                    </div>`;
            });
    });
};

/**
 * Configurar modal de detalles con carga dinámica
 * @param {string} modalId - ID del modal
 * @param {string} contentId - ID del contenedor de contenido
 * @param {string} urlPattern - Patrón de URL para cargar contenido
 */
window.setupDetailModal = function(modalId, contentId, urlPattern) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    const content = document.getElementById(contentId);
    if (!content) return;

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const itemId = button?.getAttribute('data-rol-id');
        const itemName = button?.closest('.card')?.querySelector('.card-title')?.textContent;

        if (!itemId) return;

        // Actualizar título si existe
        const nameElement = modal.querySelector('.rol-nombre-placeholder');
        if (nameElement && itemName) {
            nameElement.textContent = itemName;
        }

        // Mostrar spinner
        content.innerHTML = `
            <div class="d-flex justify-content-center align-items-center p-5">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Cargando...</span>
                </div>
            </div>`;

        // Cargar contenido
        fetch(urlPattern.replace('{id}', itemId))
            .then(response => {
                if (!response.ok) throw new Error('Error al cargar detalles');
                return response.text();
            })
            .then(html => {
                content.innerHTML = html;
            })
            .catch(error => {
                console.error('Error:', error);
                content.innerHTML = `
                    <div class="alert alert-danger m-3">
                        <i class="ti ti-alert-circle me-2"></i>
                        No se pudo cargar la información.
                    </div>`;
            });
    });
};

/**
 * Configurar modal de eliminación
 * @param {string} modalId - ID del modal
 * @param {string} formId - ID del formulario
 * @param {string} nameElementId - ID del elemento donde mostrar el nombre
 * @param {string} urlPattern - Patrón de URL para la acción
 */
window.setupDeleteModal = function(modalId, formId, nameElementId, urlPattern) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const itemId = button?.getAttribute('data-rol-id');
        const itemName = button?.getAttribute('data-rol-nombre');

        if (!itemId || !itemName) return;

        // Actualizar nombre en el modal
        const nameElement = document.getElementById(nameElementId);
        if (nameElement) {
            nameElement.textContent = itemName;
        }

        // Actualizar formulario
        const form = document.getElementById(formId);
        if (form) {
            const action = urlPattern.replace('{id}', itemId);
            form.setAttribute('action', action);
            form.setAttribute('hx-post', action);
            
            // Notificar a HTMX del cambio
            if (typeof htmx !== 'undefined') {
                htmx.process(form);
            }
        }
    });
};

/**
 * Inicializa los formularios dentro de un modal cuando se abre
 * @param {string} modalId - ID del modal
 */
window.setupModalFormValidation = function(modalId) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    // Inicializar validación cuando se abre el modal
    modal.addEventListener('shown.bs.modal', function() {
        const forms = modal.querySelectorAll('form');
        forms.forEach(form => {
            if (form.id) {
                // Reiniciar validación
                clearFormValidation(form.id);
                // Configurar validación en tiempo real
                setupRealTimeValidation(form.id);
            }
        });
    });
};

// Renderiza un panel de feedback reutilizable usando Bootstrap Alerts
window.renderFeedbackAlert = function(containerEl, type, title, message) {
    if (!containerEl) return;
    const map = {
        success: { cls: 'alert-success', icon: 'ti ti-check' },
        warning: { cls: 'alert-warning', icon: 'ti ti-alert-triangle' },
        info: { cls: 'alert-info', icon: 'ti ti-info-circle' },
        danger: { cls: 'alert-danger', icon: 'ti ti-alert-circle' }
    };
    const m = map[type] || map.info;
    containerEl.innerHTML = `
        <div class="alert ${m.cls} d-flex align-items-start gap-2 shadow-sm rounded-3 mb-2" role="status" aria-live="polite">
            <i class="${m.icon} mt-1"></i>
            <div>
                <div class="fw-semibold">${title || ''}</div>
                <div class="small">${message || ''}</div>
            </div>
        </div>`;
};
