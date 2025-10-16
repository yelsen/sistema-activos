// Verificar si IMask está disponible
if (typeof IMask === 'undefined') {
  console.warn('IMask no está cargado. Saltando inicialización de input masks.');
} else {
  !(function () {
    var e,
      n,
      t = document.getElementById("phone-mask"),
    t =
      (t &&
        ((d = IMask(t, { mask: "+{7}(000)000-00-00" })).on(
          "accept",
          function () {
            11 <= d.unmaskedValue.length &&
              ((document.getElementById("phone-complete").style.display = ""),
              (document.getElementById("phone-unmasked").innerHTML =
                d.unmaskedValue));
          }
        ),
        d.on("complete", function () {
          document.getElementById("phone-complete").style.display =
            "inline-block";
        })),
      document.getElementById("ssn-mask")),
    t =
      (t &&
        ((e = IMask(t, {
          mask: "XXX-XX-0000",
          definitions: {
            X: { mask: "0", displayChar: "X", placeholderChar: "#" },
          },
          lazy: !1,
          overwrite: "shift",
        })).on("accept", function () {
          11 === e.value.length &&
            ((document.getElementById("ssn-complete").style.display = ""),
            (document.getElementById("ssn-value").innerHTML = e.value));
        }),
        e.on("complete", function () {
          document.getElementById("ssn-complete").style.display =
            "inline-block";
        })),
      document.getElementById("regexp-mask")),
    t =
      (t && IMask(t, { mask: /^[1-6]\d{0,5}$/ }),
      document.getElementById("number-mask")),
    t =
      (t &&
        (n = IMask(t, {
          mask: Number,
          min: -1e4,
          max: 1e4,
          thousandsSeparator: " ",
        })).on("accept", function () {
          document.getElementById("number-value").innerHTML = n.masked.number;
        }),
      document.getElementById("date-mask"));
  t &&
    (a = IMask(t, {
      mask: Date,
      min: new Date(1990, 0, 1),
      max: new Date(2020, 0, 1),
      lazy: !1,
    })).on("accept", function () {
      document.getElementById("date-value").innerHTML = a.masked.date || "-";
    });
  (t = document.getElementById("dy-mask")) &&
    (a = IMask(t, {
      mask: "MM{/}YY",
      lazy: !1,
      blocks: {
        MM: { mask: IMask.MaskedRange, from: 1, to: 12, maxLength: 2 },
        YY: { mask: IMask.MaskedRange, from: 23, to: 99, maxLength: 2 },
      },
    })).on("accept", function () {
      console.log("Entered value:", a.value);
    });
  var a,
    m,
    s,
    d,
    t = document.getElementById("dynamic-mask"),
    t =
      (t &&
        (m = IMask(t, {
          mask: [{ mask: "+{7}(000)000-00-00" }, { mask: /^\S*@?\S*$/ }],
        })).on("accept", function () {
          0 < m.masked.unmaskedValue.length &&
            (document.getElementById("dynamic-value").innerHTML =
              m.masked.unmaskedValue || "-");
        }),
      document.getElementById("currency-mask")),
    t =
      (t &&
        (s = IMask(t, {
          mask: "$num",
          blocks: { num: { mask: Number, thousandsSeparator: " " } },
        })).on("accept", function () {
          document.getElementById("currency-value").innerHTML =
            s.masked.unmaskedValue || "-";
        }),
      document.getElementById("pipe-mask")),
    t =
      (t &&
        ((d = IMask.createPipe(
          d.masked,
          IMask.PIPE_TYPE.UNMASKED,
          IMask.PIPE_TYPE.MASKED
        )),
        t.addEventListener("input", function (e) {
          e = e.target.value;
          document.getElementById("pipe-value").innerHTML = d(e) || "-";
        })),
      document.getElementById("card-mask")),
    t =
      (t && new IMask(t, { mask: "0000-0000-0000-0000" }),
      document.getElementById("digit-mask")),
    t =
      (t && new IMask(t, { mask: "000" }),
      document.getElementById("discountCode"));
  t && new IMask(t, { mask: "FC-000000", lazy: !1, placeholderChar: "X" });
  })();
}
