const kanbanSections = ["#do", "#progress", "#review", "#done"].map((e) =>
  document.querySelector(e)
);
kanbanSections.some((e) => null !== e) &&
  dragula(kanbanSections.filter(Boolean));
