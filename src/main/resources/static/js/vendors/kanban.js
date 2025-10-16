// Verificar si dragula está disponible
if (typeof dragula !== 'undefined') {
  const kanbanSections = ["#do", "#progress", "#review", "#done"].map((e) =>
    document.querySelector(e)
  );
  
  if (kanbanSections.some((e) => null !== e)) {
    dragula(kanbanSections.filter(Boolean));
  } else {
    console.info('No se encontraron secciones de kanban. Saltando inicialización.');
  }
} else {
  console.warn('Dragula no está cargado. Saltando inicialización de kanban.');
}
