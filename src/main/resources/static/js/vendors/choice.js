// Función para verificar si Choices ya está inicializado
function isChoicesInitialized(element) {
  return element.hasAttribute('data-choice') || element.classList.contains('choices__input');
}

// Función para inicializar Choices de forma segura
function initializeChoices() {
  // Inicializar elementos con data-choices
  document.querySelectorAll("[data-choices]").forEach(function (e) {
    if (!isChoicesInitialized(e)) {
      var t = "input" === e.tagName.toLowerCase();
      new Choices(e, {
        allowHTML: true,
        removeItemButton: "true" === e.dataset.choicesRemoveitembutton,
        itemSelectText: "",
        maxItemCount: 5,
        searchEnabled: !1,
        placeholder: !0,
        placeholderValue: e.getAttribute("placeholder") || "Select an option",
        classNames: { containerInner: t ? "form-control" : "form-select" },
      });
    }
  });

  // Inicializar elementos con data-choices-innertext
  document.querySelectorAll("[data-choices-innertext]").forEach(function (o) {
    if (!isChoicesInitialized(o)) {
      new Choices(o, {
        allowHTML: true,
        removeItemButton: "true" === o.dataset.choicesRemoveitembutton,
        itemSelectText: "",
        searchEnabled: !1,
        placeholder: !0,
        classNames: {
          containerInner: o.dataset.choicesClassname || "form-select",
        },
        callbackOnCreateTemplates: function (c) {
          return {
            item: function (e, t) {
              var a = o.querySelector(`option[value="${t.value}"]`).dataset
                .color;
              return c(`
             <div class="${e.item} ${
                t.highlighted ? e.highlightedState : e.itemSelectable
              }"
                  data-item data-id="${t.id}" data-value="${
                t.value
              }" data-deletable>
               <span class="rounded-circle ${a} p-1 me-2 icon-shape " style="width:8px; height:8px"></span>
               ${t.label}
             </div>
           `);
            },
            choice: function (e, t) {
              var a = o.querySelector(`option[value="${t.value}"]`).dataset
                .color;
              return c(`
             <div class="${e.item} ${e.itemChoice} ${
                t.disabled ? e.itemDisabled : e.itemSelectable
              }"
                  data-select-text="${
                    this.config.itemSelectText
                  }" data-choice data-id="${t.id}" data-value="${
                t.value
              }" data-choice-selectable>
               <span class="rounded-circle ${a} p-1 me-2 icon-shape " style="width:8px; height:8px"></span>
               ${t.label}
             </div>
           `);
            },
          };
        },
      });
    }
  });
}

// Un solo event listener
document.addEventListener("DOMContentLoaded", initializeChoices);
