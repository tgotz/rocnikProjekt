$(document).ready(function () {
  $('.slick-carousel').slick({
    infinite: true,
    slidesToShow: 4,
    slidesToScroll: 1,
    dots: true,
    autoplay: true,
    autoplaySpeed: 2000,
    arrows: true,
    responsive: [
      {

        breakpoint: 1200,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 1,
        },
      },
      {
        breakpoint: 1000,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 1,
          arrows: false,
        },
      },
      {
        breakpoint: 500,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          arrows: false,
        },
      },
    ],
  });
});
//for index.jsp sroll
  window.onload = function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams.size > 0) {
    var charactersSection = document.getElementById('characters');
  charactersSection.scrollIntoView({behavior: 'smooth'});
}
};