const checkBox = () => {
  document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("[data-check-all]").forEach((c) => {
      c.addEventListener("click", function () {
        this.closest("[data-check-container]")
          .querySelectorAll('input[type="checkbox"]:not([data-check-all])')
          .forEach((e) => {
            e.checked = this.checked;
          }),
          c.removeAttribute("data-indeterminate");
      });
      const a = c
        .closest("[data-check-container]")
        .querySelectorAll('input[type="checkbox"]:not([data-check-all])');
      a.forEach((e) => {
        e.addEventListener("click", function () {
          var e = Array.from(a).every((e) => e.checked),
            t = Array.from(a).some((e) => e.checked);
          e
            ? ((c.checked = !0), c.removeAttribute("data-indeterminate"))
            : t
            ? ((c.checked = !1), c.setAttribute("data-indeterminate", "true"))
            : ((c.checked = !1), c.removeAttribute("data-indeterminate"));
        });
      });
    });
  });
};
checkBox();
