// Modals management and flash handling for Usuarios module
(function () {
  document.addEventListener('DOMContentLoaded', function () {
    // Flash messages container
    const flashContainer = document.getElementById('flash-messages');
    if (flashContainer) {
      // Toasts movidos a index (toasts.js); aquí solo apertura de modales programática

      // Open Edit modal if error for a specific user
      const errorUsuarioId = flashContainer.dataset.errorUsuarioId;
      if (errorUsuarioId) {
        const modalEditarEl = document.getElementById('modalEditarUsuario');
        if (modalEditarEl) {
          modalEditarEl.setAttribute('data-open-usuario-id', errorUsuarioId);
          const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEditarEl);
          modalInstance.show();
        }
      }

      // Open Create modal if validation failed
      const modalCrearError = flashContainer.dataset.modalCrearError;
      if (modalCrearError) {
        const modalCrearEl = document.getElementById('modalCrearUsuario');
        if (modalCrearEl) {
          const modalInstance = bootstrap.Modal.getOrCreateInstance(modalCrearEl);
          modalInstance.show();
        }
      }

      // Open Delete modal if error for a specific user
      const errorUsuarioDeleteId = flashContainer.dataset.errorUsuarioDeleteId;
      const errorUsuarioDeleteNombre = flashContainer.dataset.errorUsuarioDeleteNombre;
      if (errorUsuarioDeleteId && errorUsuarioDeleteNombre) {
        const modalEliminarEl = document.getElementById('modalEliminarUsuario');
        if (modalEliminarEl) {
          modalEliminarEl.setAttribute('data-open-usuario-id', errorUsuarioDeleteId);
          modalEliminarEl.setAttribute('data-open-usuario-nombre', errorUsuarioDeleteNombre);
          const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEliminarEl);
          modalInstance.show();
        }
      }
    }

    // Crear: reinitialize choices on show and init PersonaSearch
    const modalCrearEl = document.getElementById('modalCrearUsuario');
    if (modalCrearEl) {
      modalCrearEl.addEventListener('shown.bs.modal', () => {
        window.reinitializeChoices && window.reinitializeChoices();
        if (window.PersonaSearch) {
          window.PersonaSearch.init({
            modalId: 'modalCrearUsuario',
            formId: 'formCrearUsuario',
            saveButtonId: 'btnGuardarUsuario',
            checkField: 'tieneUsuario',
            checkFieldLabel: 'Usuario'
          });
        }
      });
    }

    // Editar: dynamic content loading, supports programmatic opening via data-open-usuario-id
    const modalEditarEl = document.getElementById('modalEditarUsuario');
    if (modalEditarEl) {
      const editarUsuarioContenido = document.getElementById('editar-usuario-contenido');
      modalEditarEl.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const usuarioId = button?.getAttribute('data-usuario-id') || modalEditarEl.getAttribute('data-open-usuario-id');
        if (!usuarioId) return;

        editarUsuarioContenido.innerHTML = `<div class="d-flex justify-content-center p-5"><div class="spinner-border text-primary" role="status"><span class="visualmente-hidden">Cargando...</span></div></div>`;

        fetch(`/seguridad/usuarios/editar/${usuarioId}`)
          .then(async response => {
            if (!response.ok) {
              const status = response.status;
              let msg = '';
              if (status === 404) msg = 'Usuario no encontrado (404).';
              else if (status === 400) msg = 'Solicitud inválida (400).';
              else if (status >= 500) msg = 'Error del servidor (' + status + ').';
              else if (status >= 300 && status < 400) msg = 'Redirección inesperada (' + status + ').';
              else msg = 'Error al cargar datos (' + status + ').';
              window.showToast && showToast(msg, 'error');
              editarUsuarioContenido.innerHTML = `<div class="alert alert-danger m-3"><i class="ti ti-alert-circle me-2"></i>${msg}</div>`;
              throw new Error(msg);
            }
            return response.text();
          })
          .then(html => {
            editarUsuarioContenido.innerHTML = html;
            if (window.reinitializeChoices) window.reinitializeChoices(editarUsuarioContenido);
            // Init PersonaSearch for Edit modal after content is loaded
            if (window.PersonaSearch) {
              // Ensure form exists before init
              const formEditar = document.getElementById('formEditarUsuario');
              if (formEditar) {
                window.PersonaSearch.init({
                  modalId: 'modalEditarUsuario',
                  formId: 'formEditarUsuario',
                  saveButtonId: 'btnActualizarUsuario',
                  checkField: 'tieneUsuario',
                  checkFieldLabel: 'Usuario'
                });
              }
            }
            modalEditarEl.removeAttribute('data-open-usuario-id');
          })
          .catch(error => {
            console.error('Error:', error);
            if (!editarUsuarioContenido.innerHTML || editarUsuarioContenido.innerHTML.trim() === '') {
              editarUsuarioContenido.innerHTML = `<div class="alert alert-danger m-3"><i class="ti ti-alert-circle me-2"></i>No se pudo cargar la información.</div>`;
            }
          });
      });
    }

    // Eliminar: set dynamic name and form action, supports programmatic opening
    const modalEliminarEl = document.getElementById('modalEliminarUsuario');
    if (modalEliminarEl) {
      modalEliminarEl.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const openId = modalEliminarEl.getAttribute('data-open-usuario-id');
        const openNombre = modalEliminarEl.getAttribute('data-open-usuario-nombre');
        const usuarioId = (button?.getAttribute('data-usuario-id')) || openId;
        const usuarioNombre = (button?.getAttribute('data-usuario-nombre')) || openNombre;
        if (!usuarioId || !usuarioNombre) return;

        modalEliminarEl.querySelector('#nombreUsuarioEliminar').textContent = usuarioNombre;
        modalEliminarEl.querySelector('#formEliminarUsuario').action = `/seguridad/usuarios/delete/${usuarioId}`;

        // reset programmatic attributes
        modalEliminarEl.removeAttribute('data-open-usuario-id');
        modalEliminarEl.removeAttribute('data-open-usuario-nombre');
      });
    }
  });

  // Optional public API
  window.UsuariosModals = {
    openEdit: function (usuarioId) {
      const modalEditarEl = document.getElementById('modalEditarUsuario');
      if (!modalEditarEl) return;
      modalEditarEl.setAttribute('data-open-usuario-id', usuarioId);
      const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEditarEl);
      modalInstance.show();
    },
    openDelete: function (usuarioId, nombre) {
      const modalEliminarEl = document.getElementById('modalEliminarUsuario');
      if (!modalEliminarEl) return;
      modalEliminarEl.setAttribute('data-open-usuario-id', usuarioId);
      modalEliminarEl.setAttribute('data-open-usuario-nombre', nombre);
      const modalInstance = bootstrap.Modal.getOrCreateInstance(modalEliminarEl);
      modalInstance.show();
    },
    openCreate: function () {
      const modalCrearEl = document.getElementById('modalCrearUsuario');
      if (!modalCrearEl) return;
      const modalInstance = bootstrap.Modal.getOrCreateInstance(modalCrearEl);
      modalInstance.show();
    }
  };
})();