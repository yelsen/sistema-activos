/**
 * Choices.js Initialization - Sistema Escalable
 * 
 * USO:
 * - Base: data-choices (select simple sin búsqueda)
 * - Modificadores opcionales:
 *   · data-choices-search: Activa búsqueda
 *   · data-choices-multiple: Activa selección múltiple
 * 
 * EJEMPLOS:
 * 1. Select simple: <select data-choices>
 * 2. Select con búsqueda: <select data-choices data-choices-search>
 * 3. Select múltiple: <select data-choices data-choices-multiple multiple>
 * 4. Select múltiple con búsqueda: <select data-choices data-choices-search data-choices-multiple multiple>
 */

// Función para verificar si Choices ya está inicializado
function isChoicesInitialized(element) {
  return element.classList.contains('choices__input') ||
    element.classList.contains('choices__inner') ||
    element.parentElement?.classList.contains('choices');
}

// Función para obtener textos en español
function getSpanishTexts() {
  return {
    itemSelectText: 'Presiona para seleccionar',
    noResultsText: 'No se encontraron resultados',
    noChoicesText: 'No hay opciones disponibles',
    loadingText: 'Cargando...',
    maxItemText: (maxItemCount) => `Solo puedes agregar ${maxItemCount} elementos`,
  };
}

// Función para unificar el comportamiento del input de búsqueda
function unifySearchInputBehavior(element, instance) {
  if (!element || !instance) return;
  
  const container = element.parentElement?.closest('.choices') || element.closest('.choices');
  if (!container) return;
  
  // Aplicar clase unificada para todos los inputs de búsqueda
  const addUnifiedSearchClass = () => {
    const searchInputs = container.querySelectorAll('.choices__input, .choices__input--cloned');
    searchInputs.forEach(input => {
      if (input) {
        input.classList.add('choices-unified-search');
      }
    });
  };
  
  // Aplicar al inicializar y cuando se muestre el dropdown
  addUnifiedSearchClass();
  element.addEventListener('showDropdown', addUnifiedSearchClass);
}

// Función principal de inicialización
function initializeChoices() {

  // Inicializar todos los elementos con data-choices
  document.querySelectorAll('[data-choices]').forEach(function (element) {
    if (isChoicesInitialized(element)) return;

    // Detectar modificadores
    const hasSearch = element.hasAttribute('data-choices-search');
    const isMultiple = element.hasAttribute('data-choices-multiple') || element.hasAttribute('multiple');
    
    // Configuración base
    const placeholder = element.getAttribute('placeholder') || (isMultiple ? 'Selecciona opciones' : 'Selecciona una opción');
    const searchPlaceholder = element.dataset.searchPlaceholder || 'Buscar...';
    const maxItems = parseInt(element.dataset.maxItems) || -1;
    const resultLimit = parseInt(element.dataset.searchLimit) || 50;
    const removeButton = element.dataset.choicesRemoveButton === 'true' || isMultiple;

    // Crear instancia de Choices con configuración dinámica
    const instance = new Choices(element, {
      allowHTML: true,
      removeItemButton: removeButton,
      // Rehabilitar búsqueda: tanto simple como múltiple si el atributo la solicita
      searchEnabled: hasSearch,
      searchPlaceholderValue: searchPlaceholder,
      searchResultLimit: resultLimit,
      shouldSort: false,
      placeholder: true,
      placeholderValue: placeholder,
      maxItemCount: isMultiple ? maxItems : -1,
      itemSelectText: '',
      // Evitar que el select múltiple se cierre al seleccionar una opción
      closeDropdownOnSelect: !isMultiple,
      classNames: {
        containerOuter: 'choices',
        containerInner: 'choices__inner',
        input: 'choices__input',
        inputCloned: 'choices__input--cloned',
        list: 'choices__list',
        listItems: 'choices__list--multiple',
        listSingle: 'choices__list--single',
        listDropdown: 'choices__list--dropdown',
        item: 'choices__item',
        itemSelectable: 'choices__item--selectable',
        itemDisabled: 'choices__item--disabled',
        itemChoice: 'choices__item--choice',
        placeholder: 'choices__placeholder',
        group: 'choices__group',
        groupHeading: 'choices__heading',
        button: 'choices__button',
        activeState: 'is-active',
        focusState: 'is-focused',
        openState: 'is-open',
        disabledState: 'is-disabled',
        highlightedState: 'is-highlighted',
        selectedState: 'is-selected',
        flippedState: 'is-flipped',
        loadingState: 'is-loading',
        noResults: 'has-no-results',
        noChoices: 'has-no-choices'
      },
      ...getSpanishTexts()
    });

    // Unificar comportamiento del input de búsqueda
    unifySearchInputBehavior(element, instance);

    // Añadir clases de variante para estilos específicos sin afectar al select simple
    let containerRef;
    try {
      containerRef = element.parentElement?.closest('.choices') || element.closest('.choices');
      if (containerRef) {
        if (hasSearch) containerRef.classList.add('choices--panel');
        if (isMultiple) containerRef.classList.add('choices--multi');
        // Exponer placeholder para estilos (especialmente en múltiple vacío)
        const placeholderText = element.getAttribute('placeholder') || (isMultiple ? 'Selecciona opciones' : 'Selecciona una opción');
        containerRef.setAttribute('data-placeholder', placeholderText);
        
        // Mejorar gestión del placeholder
        if (placeholderText) {
          const innerElement = containerRef.querySelector('.choices__inner');
          if (innerElement) {
            innerElement.setAttribute('data-placeholder', placeholderText);
          }
        }
      }
    } catch (_) { /* noop */ }

    // Unificar estilo del input clonado dentro del dropdown (simple o múltiple)
    const addSearchFieldClass = () => {
      const dropdownInput = containerRef?.querySelector('.choices__list--dropdown .choices__input--cloned');
      dropdownInput?.classList.add('choices-search-input');
    };
    element.addEventListener('showDropdown', addSearchFieldClass);

     // ==== FIX: Mover buscador al head del dropdown en múltiple con búsqueda ====
    if (isMultiple && hasSearch) {
      const container = element.parentElement?.closest('.choices') || element.closest('.choices');
      if (container) {
        // Quitar el input inline del control para evitar duplicado
        const inlineAtControl = container.querySelector('.choices__inner .choices__input');
        inlineAtControl?.remove();

        // Pasar placeholder al contenedor de chips para que CSS pueda mostrarlo cuando esté vacío
        const chipsList = container.querySelector('.choices__list--multiple');
        const ph = container.getAttribute('data-placeholder') || 'Selecciona opciones';
        chipsList?.setAttribute('data-placeholder', ph);

        const inner = container.querySelector('.choices__inner');
        const dropdown = container.querySelector('.choices__list--dropdown');

        // Obtener el input clonado que inserta Choices
        const getInlineInput = () => container.querySelector('.choices__input--cloned');

        // Head del dropdown (crear una sola vez)
        const ensureHead = () => {
          if (!dropdown) return null;
          let head = dropdown.querySelector('.choices__dropdown-head');
          if (!head) {
            head = document.createElement('div');
            head.className = 'choices__dropdown-head';
            dropdown.insertBefore(head, dropdown.firstChild);
          }
          return head;
        };

        // Al abrir: mover input al head
        const moveInputToHead = () => {
          const head = ensureHead();
          const input = getInlineInput();
          if (head && input && !head.contains(input)) {
            head.appendChild(input);
          }
          // Enfocar automáticamente el buscador como en el select simple
          const toFocus = head?.querySelector('.choices__input--cloned');
          if (toFocus) {
            setTimeout(() => {
              try { toFocus.focus(); } catch (_) { /* noop */ }
            }, 0);
          }
        };

        // Al cerrar: devolver input al inner
        const moveInputBack = () => {
          const head = dropdown?.querySelector('.choices__dropdown-head');
          const input = head?.querySelector('.choices__input--cloned');
          if (input && inner && !inner.contains(input)) {
            inner.appendChild(input);
          }
        };

        element.addEventListener('showDropdown', moveInputToHead);
        element.addEventListener('hideDropdown', moveInputBack);

        // Comportamiento de colapso al hacer click de nuevo sobre el control cuando está abierto
        const containerClickToggle = (evt) => {
          if (container.classList.contains('is-open')) {
            // Evitar que el click vuelva a abrir inmediatamente
            evt.preventDefault();
            evt.stopPropagation();
            evt.stopImmediatePropagation?.();
            instance.hideDropdown();
            // Marcar que fue cerrado manualmente para suprimir la siguiente apertura automática
            container.dataset.justClosed = 'true';
            setTimeout(() => { delete container.dataset.justClosed; }, 150);
          }
        };
        // Usamos mousedown para interceptar antes del manejo interno de Choices
        container.addEventListener('mousedown', containerClickToggle, true);
        // Si Choices intenta abrir justo después de cerrar manualmente, lo suprimimos
        element.addEventListener('showDropdown', () => {
          if (container.dataset.justClosed) {
            instance.hideDropdown();
            delete container.dataset.justClosed;
          }
        }, true);
      }
    }
    // Para múltiple con búsqueda: mover el input inline al head del dropdown al abrir,
    // y regresarlo al control al cerrar. De esta forma hay placeholder cuando está vacío
    // y el buscador aparece en el panel como en el select simple.
    if (isMultiple && hasSearch) {
      const container = element.parentElement?.closest('.choices') || element.closest('.choices');
      if (container) {
        const inner = container.querySelector('.choices__inner');
        const dropdown = container.querySelector('.choices__list--dropdown');
        const getInlineInput = () => container.querySelector('.choices__inner .choices__input');

        const ensureHead = () => {
          if (!dropdown) return null;
          let head = dropdown.querySelector('.choices__dropdown-head');
          if (!head) {
            head = document.createElement('div');
            head.className = 'choices__dropdown-head';
            head.style.position = 'sticky';
            head.style.top = '0';
            head.style.zIndex = '3';
            head.style.background = 'var(--choices-bg-color-dropdown, #fff)';
            head.style.padding = '0.5rem 0.75rem 0 0.75rem';
            dropdown.insertBefore(head, dropdown.firstChild);
          }
          return head;
        };

        const moveInlineInputToHead = () => {
          const head = ensureHead();
          const input = getInlineInput();
          if (!head || !input) return;
          if (!head.contains(input)) {
            head.appendChild(input);
          }
        };

        const moveInlineInputBack = () => {
          const input = dropdown?.querySelector('.choices__dropdown-head .choices__input');
          if (!input || !inner) return;
          if (!inner.contains(input)) {
            inner.appendChild(input);
          }
        };

        element.addEventListener('showDropdown', moveInlineInputToHead);
        element.addEventListener('hideDropdown', moveInlineInputBack);
      }
    }
  });

  // 4. SELECT CON COLORES PERSONALIZADOS - data-choices-innertext
  document.querySelectorAll('[data-choices-innertext]').forEach(function (element) {
    if (isChoicesInitialized(element)) return;

    const removeButton = element.dataset.choicesRemoveitembutton === 'true';
    const placeholder = element.getAttribute('placeholder') || 'Selecciona una opción';

    new Choices(element, {
      allowHTML: true,
      removeItemButton: removeButton,
      searchEnabled: false,
      shouldSort: false,
      placeholder: true,
      placeholderValue: placeholder,
      itemSelectText: '',
      classNames: {
        containerOuter: 'choices',
        containerInner: 'choices__inner',
        input: 'choices__input',
        inputCloned: 'choices__input--cloned',
        list: 'choices__list',
        listItems: 'choices__list--multiple',
        listSingle: 'choices__list--single',
        listDropdown: 'choices__list--dropdown',
        item: 'choices__item',
        itemSelectable: 'choices__item--selectable',
        itemDisabled: 'choices__item--disabled',
        itemChoice: 'choices__item--choice',
        placeholder: 'choices__placeholder',
        group: 'choices__group',
        groupHeading: 'choices__heading',
        button: 'choices__button',
        activeState: 'is-active',
        focusState: 'is-focused',
        openState: 'is-open',
        disabledState: 'is-disabled',
        highlightedState: 'is-highlighted',
        selectedState: 'is-selected',
        flippedState: 'is-flipped',
        loadingState: 'is-loading',
        noResults: 'has-no-results',
        noChoices: 'has-no-choices'
      },
      callbackOnCreateTemplates: function (template) {
        return {
          item: function (classNames, data) {
            const option = element.querySelector(`option[value="${data.value}"]`);
            const color = option?.dataset.color || '';

            return template(`
              <div class="${classNames.item} ${data.highlighted ? classNames.highlightedState : classNames.itemSelectable
              }"
                  data-item data-id="${data.id}" data-value="${data.value}" ${data.active ? 'aria-selected="true"' : ''} ${data.disabled ? 'aria-disabled="true"' : ''}>
                ${color ? `<span class="rounded-circle ${color} p-1 me-2 icon-shape" style="width:8px; height:8px"></span>` : ''}
                ${data.label}
              </div>
            `);
          },
          choice: function (classNames, data) {
            const option = element.querySelector(`option[value="${data.value}"]`);
            const color = option?.dataset.color || '';

            return template(`
              <div class="${classNames.item} ${classNames.itemChoice} ${data.disabled ? classNames.itemDisabled : classNames.itemSelectable
              }"
                  data-select-text="${this.config.itemSelectText}" 
                  data-choice 
                  ${data.disabled ? 'data-choice-disabled aria-disabled="true"' : 'data-choice-selectable'}
                  data-id="${data.id}" 
                  data-value="${data.value}">
                ${color ? `<span class="rounded-circle ${color} p-1 me-2 icon-shape" style="width:8px; height:8px"></span>` : ''}
                ${data.label}
              </div>
            `);
          },
        };
      },
      ...getSpanishTexts()
    });
  });
}

// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initializeChoices);
} else {
  // El DOM ya está cargado
  initializeChoices();
}

// Función global para reinicializar Choices en contenido dinámico
window.reinitializeChoices = initializeChoices;
