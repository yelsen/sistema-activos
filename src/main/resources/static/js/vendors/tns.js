if (document.getElementById("product")) {
  const a = tns({
    container: "#product",
    items: 1,
    startIndex: 1,
    navContainer: "#product-thumbnails",
    navAsThumbnails: !0,
    autoplay: !1,
    autoplayTimeout: 1e3,
    swipeAngle: !1,
    speed: 400,
    controls: !1,
  });
}
