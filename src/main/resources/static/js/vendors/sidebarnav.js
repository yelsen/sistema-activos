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
    });
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
    if (n === "") n = "index.html"; // Solo trata la raíz vacía como index.html

    // Menús a actualizar: sidebar fijo y offcanvas móvil
    var containers = document.querySelectorAll("#miniSidebar, .offcanvasNav");
    containers.forEach(function (container) {
        if (!container) return;

        // 1) Limpia estados previos
        container.querySelectorAll(".active").forEach(function (el) {
            el.classList.remove("active");
        });
        container.querySelectorAll(".dropdown-toggle[aria-expanded='true']").forEach(function (tog) {
            tog.setAttribute("aria-expanded", "false");
        });
        container.querySelectorAll(".dropdown-menu.show").forEach(function (menu) {
            menu.classList.remove("show");
        });

        // 2) Marca activo el enlace que coincide con la URL actual
        container.querySelectorAll(".nav-item .nav-link, .dropdown-item").forEach(function (e) {
            var t = e.getAttribute("href");
            if (!t || t === "#") return;

            var hrefNorm = t.startsWith("/") ? t.substring(1) : t;
            if (hrefNorm === "") hrefNorm = "index.html";

            var match = n === hrefNorm || d === e.href;
            if (match) {
                e.classList.add("active");
                var parent = e.closest(".dropdown");
                while (parent) {
                    var toggle = parent.querySelector(":scope > .dropdown-toggle");
                    if (toggle) {
                        toggle.classList.add("active");
                        toggle.setAttribute("aria-expanded", "true");
                    }
                    var menu = parent.querySelector(":scope > .dropdown-menu");
                    if (menu) {
                        menu.classList.add("show");
                    }
                    parent = parent.parentElement.closest(".dropdown");
                }
            }
        });
    });
};

// Llama una vez cuando el DOM base está listo (por si el HTML ya está inline)
document.addEventListener("DOMContentLoaded", function () {
    try {
        window.updateSidebarActive();
    } catch (e) {
        console.error("Error al activar el sidebar:", e);
    }
});
window.addEventListener("load", setSidebarHeight),
    window.addEventListener("resize", setSidebarHeight);
const observer = new MutationObserver(setSidebarHeight);
observer.observe(document.getElementById("content"), {
    childList: !0,
    subtree: !0,
});
