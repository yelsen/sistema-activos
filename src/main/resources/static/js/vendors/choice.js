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
// y obtener la instancia si existe.
function isChoicesInitialized(element) {
  if (element.choices) {
    return element.choices;
  }
  return null;
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

// Función mejorada para inicializar Choices con estandarización
function initializeChoices() {
  // Configuración estándar para todos los selects
  const standardConfig = {
    allowHTML: true,
    shouldSort: false,
    itemSelectText: '',
  };

  // Inicializar todos los elementos con data-choices
  document.querySelectorAll('[data-choices]').forEach(function(element) {
    // Si ya existe una instancia de Choices, la destruimos primero
    // para evitar errores y fugas de memoria.
    const existingInstance = isChoicesInitialized(element);
    if (existingInstance) {
      existingInstance.destroy();
    }
    
    const hasSearch = element.hasAttribute('data-choices-search');
    const isMultiple = element.hasAttribute('multiple');
    
    // Manejar placeholder
    const placeholder = element.getAttribute('placeholder');
    const defaultOption = element.querySelector('option[selected], option:first-child')?.textContent || '';
    
    // Configuración específica
    const config = {
      ...standardConfig,
      ...getSpanishTexts(element),
      placeholder: !!placeholder, // Habilitar placeholder solo si está definido
      placeholderValue: placeholder || defaultOption,
      searchEnabled: hasSearch,
      searchPlaceholderValue: 'Buscar', // Texto unificado para buscadores
      removeItemButton: isMultiple,
      maxItemCount: isMultiple ? (parseInt(element.dataset.maxItems) || -1) : -1,
      closeDropdownOnSelect: !isMultiple
    };
    
    new Choices(element, config);
  });
}

// 4. SELECT CON COLORES PERSONALIZADOS - data-choices-innertext
document.querySelectorAll('[data-choices-innertext]').forEach(function (element) {
  const existingInstance = isChoicesInitialized(element);
  if (existingInstance) {
    existingInstance.destroy();
  }

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
    ...getSpanishTexts(element)
  });
});


// Inicializar cuando el DOM esté listo
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initializeChoices);
} else {
  // El DOM ya está cargado
  initializeChoices();
}

// Función global para reinicializar Choices en contenido dinámico
window.reinitializeChoices = initializeChoices;
