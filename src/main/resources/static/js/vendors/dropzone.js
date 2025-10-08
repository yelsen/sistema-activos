Dropzone.autoDiscover = !1;
const myDropzone = new Dropzone("#my-dropzone", {
  url: "https://httpbin.org/post",
  maxFilesize: 5,
  acceptedFiles: "image/*",
  addRemoveLinks: !0,
  autoProcessQueue: !0,
});
myDropzone.on("addedfile", function (o) {
  console.log("File added: " + o.name),
    setTimeout(() => {
      var e = o.previewElement.querySelector(".dz-remove");
      e && e.classList.add("btn", "btn-subtle-primary", "btn-sm", "mt-1");
    }, 10);
}),
  myDropzone.on("removedfile", function (e) {
    console.log("File removed: " + e.name);
  }),
  myDropzone.on("success", function (e, o) {
    console.log("File uploaded successfully:", o);
  });
