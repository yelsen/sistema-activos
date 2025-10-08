function initializeQuantityControls() {
  var t = document.querySelectorAll(".quantity-btn.minus"),
    n = document.querySelectorAll(".quantity-btn.plus");
  t.forEach((t) => {
    t.addEventListener("click", function () {
      var t = this.nextElementSibling,
        n = parseInt(t.value);
      n > parseInt(t.min) && (t.value = n - 1);
    });
  }),
    n.forEach((t) => {
      t.addEventListener("click", function () {
        var t = this.previousElementSibling,
          n = parseInt(t.value);
        t.value = n + 1;
      });
    });
}
initializeQuantityControls();
