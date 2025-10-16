/**
 * Choices.js Initialization
 * Soporta 3 tipos de select:
 * 1. data-choices: Select simple sin búsqueda
 * 2. data-choices-search: Select con búsqueda
 * 3. data-choices-multiple: Select con búsqueda y selección múltiple
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

// Función principal de inicialización
function initializeChoices() {

  // 1. SELECT SIMPLE (sin búsqueda) - data-choices
  document.querySelectorAll('[data-choices]').forEach(function (element) {
    if (isChoicesInitialized(element)) return;

    const isInput = element.tagName.toLowerCase() === 'input';
    const removeButton = element.dataset.choicesRemoveitembutton === 'true';
    const placeholder = element.getAttribute('placeholder');
    
    // Si no hay placeholder y es un select, usar el primer option como valor por defecto
    const hasPlaceholder = placeholder !== null && placeholder !== '';
    const placeholderValue = hasPlaceholder ? placeholder : 'Selecciona una opción';

    new Choices(element, {
      allowHTML: true,
      removeItemButton: removeButton,
      searchEnabled: false, // Sin búsqueda
      shouldSort: false,
      placeholder: hasPlaceholder,
      placeholderValue: placeholderValue,
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
      ...getSpanishTexts()
    });
  });

  // 2. SELECT CON BÚSQUEDA - data-choices-search
  document.querySelectorAll('[data-choices-search]').forEach(function (element) {
    if (isChoicesInitialized(element)) return;

    const isInput = element.tagName.toLowerCase() === 'input';
    const removeButton = element.dataset.choicesRemoveitembutton === 'true';
    const placeholder = element.getAttribute('placeholder') || 'Selecciona una opción';
    const searchPlaceholder = element.dataset.searchPlaceholder || 'Buscar...';
    const resultLimit = parseInt(element.dataset.searchLimit) || 10;

    new Choices(element, {
      allowHTML: true,
      removeItemButton: removeButton,
      searchEnabled: true, // Con búsqueda
      searchPlaceholderValue: searchPlaceholder,
      searchResultLimit: resultLimit,
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
      ...getSpanishTexts()
    });
  });

  // 3. SELECT MÚLTIPLE CON BÚSQUEDA - data-choices-multiple
  document.querySelectorAll('[data-choices-multiple]').forEach(function (element) {
    if (isChoicesInitialized(element)) return;

    const placeholder = element.getAttribute('placeholder') || 'Selecciona múltiples opciones';
    const searchPlaceholder = element.dataset.searchPlaceholder || 'Buscar...';
    const maxItems = parseInt(element.dataset.maxItems) || -1;
    const resultLimit = parseInt(element.dataset.searchLimit) || 10;

    new Choices(element, {
      allowHTML: true,
      removeItemButton: true, // Siempre con botón para remover
      searchEnabled: true, // Con búsqueda
      searchPlaceholderValue: searchPlaceholder,
      searchResultLimit: resultLimit,
      shouldSort: false,
      placeholder: true,
      placeholderValue: placeholder,
      maxItemCount: maxItems,
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
      ...getSpanishTexts()
    });
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
