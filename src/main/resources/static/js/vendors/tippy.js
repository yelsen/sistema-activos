// Verificar si tippy y Popper están disponibles antes de usarlo
if (typeof tippy !== 'undefined' && typeof Popper !== 'undefined') {
  // Inicializar solo si existen elementos con estas clases
  const imgTooltips = document.querySelectorAll(".imgtooltip");
  const textTooltips = document.querySelectorAll(".texttooltip");

  if (imgTooltips.length > 0) {
    try {
      tippy(".imgtooltip", {
        content(t) {
          (t = t.getAttribute("data-template")), (t = document.getElementById(t));
          return t ? t.innerHTML : "Template not found";
        },
        allowHTML: !0,
        theme: "light",
        animation: "scale",
      });
    } catch (error) {
      console.error('Error inicializando tippy para .imgtooltip:', error);
    }
  }

  if (textTooltips.length > 0) {
    try {
      tippy(".texttooltip", {
        content(t) {
          t = t.getAttribute("data-template");
          return document.getElementById(t).innerHTML;
        },
        allowHTML: !0,
        theme: "light",
        animation: "scale",
      });
    } catch (error) {
      console.error('Error inicializando tippy para .texttooltip:', error);
    }
  }
  
  if (!imgTooltips.length && !textTooltips.length) {
    console.info('No se encontraron elementos con tooltips. Saltando inicialización.');
  }
} else {
  if (typeof tippy === 'undefined') {
    console.warn('Tippy.js no está cargado. Saltando inicialización de tooltips.');
  }
  if (typeof Popper === 'undefined') {
    console.warn('Popper.js no está cargado (requerido por Tippy.js). Saltando inicialización.');
  }
}
