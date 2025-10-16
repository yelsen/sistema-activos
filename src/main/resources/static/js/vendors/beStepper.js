"use strict";
function elementExists(e) {
  return null !== document.querySelector(e);
}

// Verificar si Stepper está disponible
if (typeof Stepper !== 'undefined') {
  var stepperForm;
  
  if (elementExists("#stepperForm")) {
    document.addEventListener("DOMContentLoaded", function () {
      stepperForm = new Stepper(document.querySelector("#stepperForm"), {
        linear: !1,
        animation: !0,
      });
    });
  } else {
    console.info('Elemento #stepperForm no encontrado. Saltando inicialización.');
  }
} else {
  console.warn('Stepper no está cargado. Saltando inicialización.');
}
