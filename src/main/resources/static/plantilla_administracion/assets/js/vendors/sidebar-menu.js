"use strict";
!(function () {
  var t = window.location.href,
    n = t.replace(
      window.location.protocol + "//" + window.location.host + "/",
      ""
    );
  document.querySelectorAll("ul#sidebarnav a").forEach(function (e) {
    if (e.href === t || e.href === n)
      for (
        var a = e;
        a.parentElement && !a.parentElement.classList.contains("sidebar-nav");

      )
        "LI" === a.parentElement.tagName &&
          (a.classList.add("active"), a.parentElement.classList.add("active")),
          "UL" !== a.parentElement.tagName ||
            a.parentElement.classList.contains("sidebarnav") ||
            a.parentElement.classList.add("in"),
          (a = a.parentElement);
  });
})();
