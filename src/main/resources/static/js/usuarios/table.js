(() => {
  document.addEventListener('DOMContentLoaded', function () {
    const listContainer = document.getElementById('usuarios-list-container');
    const searchInput = document.getElementById('searchInput');
    const estadoSelect = document.getElementById('estado');

    let debounceTimer;
    let controller;

    function toFragmentUrl(href) {
      const u = new URL(href, window.location.origin);
      u.searchParams.set('fragment', 'true');
      return u.pathname + '?' + u.searchParams.toString();
    }

    function sanitizeUrlForHistory(href) {
      const u = new URL(href, window.location.origin);
      u.searchParams.delete('fragment');
      const params = u.searchParams.toString();
      return u.pathname + (params ? ('?' + params) : '');
    }

    function fetchUsuariosByHref(href) {
      if (!listContainer) return;

      if (controller) {
        controller.abort();
      }
      controller = new AbortController();

      listContainer.classList.add('loading');
      const fragmentUrl = toFragmentUrl(href);

      return fetch(fragmentUrl, { signal: controller.signal })
        .then((response) => {
          if (!response.ok) {
            throw new Error('Error ' + response.status + ': ' + response.statusText);
          }
          return response.text();
        })
        .then((html) => {
          listContainer.innerHTML = html;
          if (window.reinitializeChoices) {
            window.reinitializeChoices();
          }
          history.pushState(null, '', sanitizeUrlForHistory(href));
        })
        .catch((error) => {
          if (error.name !== 'AbortError') {
            console.error('Error al cargar usuarios:', error);
            listContainer.innerHTML = '<div class="alert alert-danger">Error al cargar los usuarios. Por favor, recarga la página.</div>';
          }
        })
        .finally(() => {
          listContainer.classList.remove('loading');
        });
    }

    function buildHrefFromLocation(overrides = {}) {
      const u = new URL(window.location.href);
      // Aseguramos que la ruta sea la de usuarios (por si estamos en otra ruta dentro del módulo)
      if (!u.pathname.endsWith('/seguridad/usuarios')) {
        u.pathname = '/seguridad/usuarios';
      }

      if (overrides.query !== undefined) {
        const q = (overrides.query || '').trim();
        if (q) u.searchParams.set('query', q);
        else u.searchParams.delete('query');
      }

      if (overrides.estado !== undefined) {
        const e = overrides.estado;
        if (e) u.searchParams.set('estado', e);
        else u.searchParams.delete('estado');
      }

      if (overrides.page !== undefined) {
        u.searchParams.set('page', String(overrides.page));
      } else if (!u.searchParams.has('page')) {
        u.searchParams.set('page', '0');
      }

      if (overrides.size !== undefined) {
        u.searchParams.set('size', String(overrides.size));
      } else if (!u.searchParams.has('size')) {
        const defaultSize = window.APP_CONFIG?.pagination?.defaultSize || 10;
        u.searchParams.set('size', String(defaultSize));
      }

      return u.href;
    }

    // Búsqueda
    if (searchInput) {
      searchInput.addEventListener('input', () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
          const href = buildHrefFromLocation({ query: (searchInput.value || '').trim(), page: 0 });
          fetchUsuariosByHref(href);
        }, 350);
      });
    }

    // Filtro por estado
    if (estadoSelect) {
      estadoSelect.addEventListener('change', () => {
        const href = buildHrefFromLocation({ estado: estadoSelect.value || '', page: 0 });
        fetchUsuariosByHref(href);
      });
    }

    // Delegación de eventos para paginación y tamaño de página
    if (listContainer) {
      listContainer.addEventListener('click', (e) => {
        const pageLink = e.target.closest('a.page-link');
        if (pageLink && !pageLink.closest('.disabled')) {
          e.preventDefault();
          fetchUsuariosByHref(pageLink.href);
        }
      });

      listContainer.addEventListener('change', (e) => {
        if (e.target && e.target.id === 'pageSizeSelect') {
          const newSize = parseInt(e.target.value, 10);
          const href = buildHrefFromLocation({ size: isNaN(newSize) ? undefined : newSize, page: 0 });
          fetchUsuariosByHref(href);
        }
      });
    }

    // API pública por si se necesita desde otros scripts
    window.UsuariosTable = {
      fetchByUrl: fetchUsuariosByHref,
    };
  });
})();