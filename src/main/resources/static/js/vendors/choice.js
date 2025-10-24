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
function getSpanishTexts(element) {
  const showSelectText = element && element.hasAttribute('data-show-select-text');

  return {
    itemSelectText: showSelectText ? 'Presiona para seleccionar' : '',
    noResultsText: 'No se encontraron resultados',
    noChoicesText: 'No hay opciones disponibles',
    loadingText: 'Cargando...',
    maxItemText: (maxItemCount) => `Solo puedes agregar ${maxItemCount} elementos`
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
    
    // Lógica profesional para placeholders: Si el select tiene un atributo 'placeholder'
    // y no tiene ya una opción vacía, la inyectamos dinámicamente.
    // Esta opción es necesaria para que la validación 'required' funcione.
    if (element.hasAttribute('placeholder') && !element.querySelector('option[value=""]')) {
      element.insertAdjacentHTML('afterbegin', '<option value=""></option>');
    }

    // Detectar modificadores
    const hasSearch = element.hasAttribute('data-choices-search');
    const isMultiple = element.hasAttribute('data-choices-multiple') || element.hasAttribute('multiple');

    // Configuración base
    const placeholder = element.getAttribute('placeholder');
    const searchPlaceholder = element.dataset.searchPlaceholder || 'Buscar...';
    const maxItems = parseInt(element.dataset.maxItems) || -1;
    const removeButton = element.dataset.choicesRemoveButton === 'true' || isMultiple;
    const instance = new Choices(element, {
      allowHTML: true,
      removeItemButton: removeButton,
      searchEnabled: hasSearch,
      searchPlaceholderValue: searchPlaceholder,
      shouldSort: false,
      placeholder: !!placeholder,
      placeholderValue: placeholder,
      maxItemCount: isMultiple ? maxItems : -1,
      itemSelectText: '',
      closeDropdownOnSelect: !isMultiple,
      ...getSpanishTexts(element),
    });
    if (hasSearch) {
      unifySearchInputBehavior(element, instance);
    }
  });

  // Inicializar selects con colores (si los tienes)
  initializeChoicesWithColors();
}

function initializeChoicesWithColors() {
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
      ...getSpanishTexts(element),
      callbackOnCreateTemplates: function (template) {
        return {
          item: function (classNames, data) {
            const option = element.querySelector(`option[value="${data.value}"]`);
            const color = option?.dataset.color || '';
            return template(
              `<div class="${classNames.item} ${data.highlighted ? classNames.highlightedState : classNames.itemSelectable}" data-item data-id="${data.id}" data-value="${data.value}" ${data.active ? 'aria-selected="true"' : ''} ${data.disabled ? 'aria-disabled="true"' : ''}>
                ${color ? `<span class="rounded-circle ${color} p-1 me-2 icon-shape" style="width:8px; height:8px"></span>` : ''}
                ${data.label}
              </div>`
            );
          },
          choice: function (classNames, data) {
            const option = element.querySelector(`option[value="${data.value}"]`);
            const color = option?.dataset.color || '';
            return template(
              `<div class="${classNames.item} ${classNames.itemChoice} ${data.disabled ? classNames.itemDisabled : classNames.itemSelectable}" data-select-text="${this.config.itemSelectText}" data-choice ${data.disabled ? 'data-choice-disabled aria-disabled="true"' : 'data-choice-selectable'} data-id="${data.id}" data-value="${data.value}">
                ${color ? `<span class="rounded-circle ${color} p-1 me-2 icon-shape" style="width:8px; height:8px"></span>` : ''}
                ${data.label}
              </div>`
            );
          },
        };
      },
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
