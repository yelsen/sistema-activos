class PasswordToggler {
  constructor(s, e) {
    (this.passwordElements = document.getElementsByClassName(s)),
      (this.togglerElements = document.getElementsByClassName(e)),
      this.attachEventListeners();
  }
  attachEventListeners() {
    for (let s = 0; s < this.togglerElements.length; s++)
      this.togglerElements[s].addEventListener("click", () => {
        this.showHidePassword(s);
      });
  }
  showHidePassword(s) {
    var e = this.passwordElements[s],
      s = this.togglerElements[s];
    "password" === e.type
      ? (e.setAttribute("type", "text"),
        s.classList.add("ti-eye"),
        s.classList.remove("ti-eye-off"))
      : (s.classList.remove("ti-eye"),
        s.classList.add("ti-eye-off"),
        e.setAttribute("type", "password"));
  }
}
const passwordToggler = new PasswordToggler("fakePassword", "passwordToggler");
