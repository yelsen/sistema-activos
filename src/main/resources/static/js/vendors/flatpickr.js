// Verificar si flatpickr está disponible
if (typeof flatpickr !== 'undefined') {
  const flatpickrElements = document.querySelectorAll(".flatpickr");
  
  if (flatpickrElements.length) {
    flatpickrElements.forEach((e) => {
      flatpickr(e, { disableMobile: !0 });
    });
  }
  
  const timepickrElements = document.querySelectorAll(".timepickr");
  
  if (timepickrElements.length) {
    timepickrElements.forEach((e) => {
      flatpickr(e, { enableTime: !0, noCalendar: !0, dateFormat: "H:i" });
    });
  }
  
  if (!flatpickrElements.length && !timepickrElements.length) {
    console.info('No se encontraron elementos .flatpickr o .timepickr. Saltando inicialización.');
  }
} else {
  console.warn('Flatpickr no está cargado. Saltando inicialización.');
}
