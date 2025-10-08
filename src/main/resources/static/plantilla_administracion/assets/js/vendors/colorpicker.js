document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".color-palette").forEach(function (n) {
    n.addEventListener("click", function () {
      var e,
        t,
        o = this.querySelector(".color-swatch-body-value");
      o
        ? ((e = o.textContent.trim()),
          navigator.clipboard
            .writeText(e)
            .then(function () {
              console.log("Copied to clipboard:", e);
            })
            .catch(function () {
              fallbackCopyTextToClipboard(e);
            }),
          ((t = document.createElement("div")).className = "customTooltip"),
          (t.textContent = "Copied! " + e),
          (o = n.getBoundingClientRect()),
          (t.style.position = "absolute"),
          (t.style.position = "absolute"),
          (t.style.top = o.top + window.pageYOffset + "px"),
          (t.style.left = o.left + window.pageXOffset + "px"),
          document.body.appendChild(t),
          (t.style.transform = "translate(28%, 28%)"),
          setTimeout(function () {
            t.parentNode.removeChild(t);
          }, 1e3))
        : console.error("Color code element not found!");
    });
  });
});
