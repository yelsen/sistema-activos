document.getElementById("uploadCard").addEventListener("click", function () {
  var e = document.getElementById("fileInput");
  e.click(),
    e.addEventListener(
      "change",
      function () {
        var e = 0 < this.files.length ? this.files[0].name : "No file chosen";
        document.getElementById("fileLabel").textContent = e;
      },
      { once: !0 }
    );
});
