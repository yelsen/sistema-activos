document.addEventListener("DOMContentLoaded", function () {
  var e = document.getElementById("calendar");
  const t = document.getElementById("calendar-title"),
    n = document.getElementById("add-new-event-btn"),
    d = document.getElementById("update-event-btn");
  var a = document.getElementById("btn-delete-event"),
    l = document.getElementById("btn-edit-event"),
    o = document.getElementById("create-new-event-btn"),
    c = document.getElementById("today-btn"),
    i = document.getElementById("prev-month"),
    m = document.getElementById("next-month"),
    s = document.getElementById("calendar-view");
  const r = new bootstrap.Modal(
      document.getElementById("add-edit-event-modal")
    ),
    v = new bootstrap.Modal(document.getElementById("view-event-modal")),
    u = [
      {
        id: 1,
        title: "Company Meeting",
        start: moment().startOf("month").add(5, "days").format("YYYY-MM-DD"),
        description: "Discuss project updates",
        className: "bg-primary-subtle text-primary-emphasis",
      },
      {
        id: 2,
        title: "Product Launch",
        start: moment().startOf("month").add(12, "days").format("YYYY-MM-DD"),
        end: moment().startOf("month").add(15, "days").format("YYYY-MM-DD"),
        description: "Launch new product",
        className: "bg-success-subtle text-success-emphasis",
      },
    ];
  let E = null;
  var g = { enableTime: !0, dateFormat: "Y-m-d H:i" };
  flatpickr("#eventStart", g), flatpickr("#eventEnd", g);
  const y = new FullCalendar.Calendar(e, {
      headerToolbar: !1,
      initialView: "dayGridMonth",
      initialDate: moment().format("YYYY-MM-DD"),
      editable: !0,
      selectable: !0,
      events: u,
      height: "65vh",
      eventClick: function (e) {
        (E = e.event),
          (document.getElementById("view-event-modal-title").innerText =
            E.title),
          (document.getElementById("view-event-description").innerText =
            E.extendedProps.description || "No description"),
          (document.getElementById("view-event-dates").innerText =
            E.start && E.end
              ? E.start.toLocaleString() + " - " + E.end.toLocaleString()
              : E.start.toLocaleString()),
          (document.getElementById("view-event-location").innerText =
            E.extendedProps.location || "No location specified"),
          (document.getElementById("selected-event-id").value = E.id),
          v.show();
      },
      dateClick: function (e) {
        B(),
          document.getElementById("eventStart")._flatpickr.setDate(e.date),
          n.classList.remove("d-none"),
          d.classList.add("d-none"),
          r.show();
      },
    }),
    p =
      (y.render(),
      () => {
        t.textContent = y.view.title;
      }),
    B =
      (p(),
      i.addEventListener("click", () => {
        y.prev(), p();
      }),
      m.addEventListener("click", () => {
        y.next(), p();
      }),
      c.addEventListener("click", () => {
        y.today(), p();
      }),
      s.addEventListener("change", (e) => {
        y.changeView(e.target.value), p();
      }),
      o.addEventListener("click", () => {
        B(), n.classList.remove("d-none"), d.classList.add("d-none"), r.show();
      }),
      n.addEventListener("click", (e) => {
        e.preventDefault();
        e = {
          id: u.length + 1,
          title: document.getElementById("event-title").value,
          start: document.getElementById("eventStart").value,
          end: document.getElementById("eventEnd").value || null,
          description: document.getElementById("event-description").value,
          location: document.getElementById("event-location").value,
          className: document.getElementById("event-category").value,
        };
        y.addEvent(e), u.push(e), r.hide(), B();
      }),
      d.addEventListener("click", (e) => {
        e.preventDefault(),
          E &&
            (E.setProp("title", document.getElementById("event-title").value),
            E.setStart(document.getElementById("eventStart").value),
            E.setEnd(document.getElementById("eventEnd").value || null),
            E.setExtendedProp(
              "description",
              document.getElementById("event-description").value
            ),
            E.setExtendedProp(
              "location",
              document.getElementById("event-location").value
            ),
            E.setProp("classNames", [
              document.getElementById("event-category").value,
            ]),
            r.hide(),
            B());
      }),
      a.addEventListener("click", () => {
        E && (E.remove(), v.hide());
      }),
      l.addEventListener("click", () => {
        E &&
          ((document.getElementById("event-title").value = E.title),
          document.getElementById("eventStart")._flatpickr.setDate(E.start),
          document.getElementById("eventEnd")._flatpickr.setDate(E.end || null),
          (document.getElementById("event-description").value =
            E.extendedProps.description || ""),
          (document.getElementById("event-location").value =
            E.extendedProps.location || ""),
          (document.getElementById("event-category").value = E.classNames[0]),
          n.classList.add("d-none"),
          d.classList.remove("d-none"),
          r.show(),
          v.hide());
      }),
      () => {
        (document.getElementById("event-title").value = ""),
          document.getElementById("eventStart")._flatpickr.clear(),
          document.getElementById("eventEnd")._flatpickr.clear(),
          (document.getElementById("event-description").value = ""),
          (document.getElementById("event-location").value = ""),
          (document.getElementById("event-category").value =
            "bg-primary-subtle"),
          (E = null);
      });
});
