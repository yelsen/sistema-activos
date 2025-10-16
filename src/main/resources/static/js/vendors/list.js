document.addEventListener("DOMContentLoaded", function () {
  // Verificar si List.js está disponible
  if (typeof List === 'undefined') {
    console.warn('List.js no está cargado. Saltando inicialización.');
    return;
  }
  
  const listElements = document.querySelectorAll("[data-list]");
  
  if (!listElements.length) {
    console.info('No se encontraron elementos [data-list]. Saltando inicialización.');
    return;
  }
  
  listElements.forEach((n) => {
    var a, s, l, e;
    function t(e) {
      var t = e.items.length,
        e = (e.visibleItems.length, t + " entries found"),
        t = n.querySelector(".listjs-showing-items-label");
      t && (t.innerHTML = e);
    }
    function i() {
      var e = Math.ceil(a.items.length / l),
        t = n.querySelector(".prev"),
        i = n.querySelector(".next");
      t && (t.disabled = 1 === s), i && (i.disabled = s === e);
    }
    n.id &&
      ((e = n.getAttribute("data-list").split(",")),
      t(
        (a = new List(n.id, {
          valueNames: e,
          page: 10,
          searchClass: "listjs-search",
          sortClass: "listjs-sorter",
          pagination: [
            {
              name: "pagination",
              paginationClass: "pagination",
              left: 1,
              right: 1,
              item: '<li class="page-item"><a class="page-link page" href="#"></a></li>',
            },
          ],
        }))
      ),
      a.on("updated", function () {
        t(a);
      }),
      (e = n.querySelector(".listjs-items-per-page")) &&
        e.addEventListener("change", function () {
          var e = this.value;
          (a.page = e), a.update();
        }),
      (s = 1),
      (l = 10),
      (e = n.querySelector(".prev")) &&
        e.addEventListener("click", function () {
          1 < s && (s--, a.show((s - 1) * l, l), i());
        }),
      (e = n.querySelector(".next")) &&
        e.addEventListener("click", function () {
          s * l < a.items.length && (s++, a.show((s - 1) * l, l), i());
        }),
      i());
  });
});
