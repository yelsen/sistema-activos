// Verificar si Quill está disponible y si existe el elemento
if (typeof Quill !== 'undefined') {
  var quill,
    editorElement = document.querySelector("#editor");
  
  if (editorElement) {
    quill = new Quill(editorElement, {
      modules: {
        toolbar: [
          [{ header: [1, 2, !1] }],
          [{ font: [] }],
          ["bold", "italic", "underline", "strike"],
          [{ size: ["small", !1, "large", "huge"] }],
          [{ list: "ordered" }, { list: "bullet" }],
          [{ color: [] }, { background: [] }, { align: [] }],
          ["link", "image", "code-block", "video"],
        ],
      },
      theme: "snow",
    });
  } else {
    console.info('Elemento #editor no encontrado. Saltando inicialización de Quill.');
  }
} else {
  console.warn('Quill no está cargado. Saltando inicialización del editor.');
}
