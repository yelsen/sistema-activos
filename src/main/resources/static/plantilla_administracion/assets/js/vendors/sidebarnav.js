function setSidebarHeight() {
  var e,
    t = document.getElementById("miniSidebar"),
    d = document.getElementById("content");
  t &&
    d &&
    ((d = d.getBoundingClientRect().height),
    (e = window.innerHeight),
    (t.style.height = Math.max(e - 45, d) + "px"));
}
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("miniSidebar"), document.getElementById("content");
  var e = document.querySelectorAll(".sidebar-toggle");
  localStorage.getItem("sidebarExpanded");
  e.forEach((e) => {
    e.addEventListener("click", () => {
      "true" === localStorage.getItem("sidebarExpanded")
        ? (document.documentElement.classList.add("collapsed"),
          document.documentElement.classList.remove("expanded"),
          localStorage.setItem("sidebarExpanded", "false"))
        : (document.documentElement.classList.remove("collapsed"),
          document.documentElement.classList.add("expanded"),
          localStorage.setItem("sidebarExpanded", "true"));
    });
  }),
    document.querySelectorAll(".dropdown-submenu").forEach((n) => {
      n.addEventListener("click", (e) => {
        var t = n.querySelector(".dropdown-menu"),
          d = e.target.closest("a");
        (d && "#!" !== d.getAttribute("href")) ||
          (e.stopPropagation(),
          e.preventDefault(),
          t.classList.toggle("show", isVisible));
      });
    });
});

// Expone una función reutilizable para recomputar el activo tras cargas dinámicas
window.updateSidebarActive = function () {
  var d = window.location.href,
    n = window.location.pathname;
  // Normaliza pathname (quita la barra inicial) y trata index.html como raíz
  n = n.startsWith("/") ? n.substring(1) : n;
  if (n === "" || n.endsWith("/")) n = "index.html";

  // Menús a actualizar: sidebar fijo y offcanvas móvil
  var containers = document.querySelectorAll("#miniSidebar, .offcanvasNav");
  containers.forEach(function (container) {
    if (!container) return;

    // 1) Limpia estados previos
    container.querySelectorAll(".active").forEach(function (el) {
      el.classList.remove("active");
    });
    container
      .querySelectorAll(".dropdown-toggle[aria-expanded='true']")
      .forEach(function (tog) {
        tog.setAttribute("aria-expanded", "false");
      });
    container.querySelectorAll(".dropdown-menu.show").forEach(function (menu) {
      menu.classList.remove("show");
    });

    // 2) Marca activo el enlace que coincide con la URL actual (soporta rutas relativas)
    container
      .querySelectorAll(".nav-item .nav-link, .dropdown-item")
      .forEach(function (e) {
        var t = e.getAttribute("href");
        if (!t || t === "#") return;
        // Normaliza href comparado
        var hrefNorm = t.startsWith("/") ? t.substring(1) : t;
        if (hrefNorm === "" || hrefNorm.endsWith("/")) hrefNorm = "index.html";

        // Coincide si el pathname o el href absoluto terminan con el href del enlace
        var match = n.endsWith(hrefNorm) || d.endsWith(hrefNorm) || hrefNorm === n || e.href === d;
        if (match) {
          // Activa el enlace
          e.classList.add("active");

          // Si está dentro de un dropdown: activa el toggle padre y abre el menú
          var dropdown = e.closest(".dropdown");
          if (dropdown) {
            var toggle = dropdown.querySelector(".dropdown-toggle");
            var menu = toggle && toggle.nextElementSibling;
            if (toggle) {
              toggle.classList.add("active");
              toggle.setAttribute("aria-expanded", "true");
            }
            if (menu) {
              menu.classList.add("show");
            }
          }

          // Si está dentro de un submenú anidado
          var subDropdown = e.closest(".dropdown-submenu");
          if (subDropdown) {
            var subToggle = subDropdown.querySelector(".dropdown-toggle");
            var subMenu = subToggle && subToggle.nextElementSibling;
            if (subToggle) {
              subToggle.classList.add("active");
              subToggle.setAttribute("aria-expanded", "true");
            }
            if (subMenu) {
              subMenu.classList.add("show");
            }
          }
        }
      });
  });
};

// Llama una vez cuando el DOM base está listo (por si el HTML ya está inline)
document.addEventListener("DOMContentLoaded", function () {
  try { window.updateSidebarActive(); } catch (e) {}
});
  window.addEventListener("load", setSidebarHeight),
  window.addEventListener("resize", setSidebarHeight);
const observer = new MutationObserver(setSidebarHeight);
observer.observe(document.getElementById("content"), {
  childList: !0,
  subtree: !0,
});
