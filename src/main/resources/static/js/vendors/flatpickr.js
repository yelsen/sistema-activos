const flatpickrElements = document.querySelectorAll(".flatpickr"),
  timepickrElements =
    (flatpickrElements.length &&
      flatpickrElements.forEach((e) => {
        flatpickr(e, { disableMobile: !0 });
      }),
    document.querySelectorAll(".timepickr"));
timepickrElements.length &&
  timepickrElements.forEach((e) => {
    flatpickr(e, { enableTime: !0, noCalendar: !0, dateFormat: "H:i" });
  });
