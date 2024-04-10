$(document).ready(function(){
  $('.slick-carousel').slick({
    infinite: true,
  slidesToShow: 4,
  slidesToScroll: 1,
  dots:true,
  autoplay:true,
  autoplaySpeed: 2000,
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
        },
      },
      {
        breakpoint: 500,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
        },
      },
    ],
  });
});