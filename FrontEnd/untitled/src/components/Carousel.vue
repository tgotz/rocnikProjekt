<template>
  <div class="carousel-wrapper">
    <h2 class="mt-5 outline-heading">Nově přidané postavy...</h2>

    <div class="slick-carousel">
      <div
          v-for="character in recentCharacters"
          :key="character.id"
          class="text-center slick-carousel-item"
      >
        <a :href="'/detail/' + character.id">
          <img
              class="character-image"
              :src="'data:image/jpeg;base64,' + character.imageBytes"
              :alt="character.name"
          />
          <div class="caption caption-carousel">
            <h3 class="my-0">{{ character.name }}</h3>
            <p class="my-0">{{ character.filmName }}</p>
          </div>
        </a>
      </div>
    </div>

    <p class="d-flex justify-content-center mt-5">
      <a href="#database">
        <i class="arrow"></i>
      </a>
    </p>
  </div>
</template>

<script>
import $ from 'jquery';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import 'slick-carousel';

export default {
  props: {
    characters: {
      type: Array,
      required: true,
    },
  },
  computed: {
    recentCharacters() {
      return this.characters
          .slice()
          .sort((a, b) => new Date(b.dateAdded) - new Date(a.dateAdded))
          .slice(0, 6);
    },
  },
  mounted() {
    console.log('Carousel DOM before Slick initialization:', this.$el.innerHTML);

    this.$nextTick(() => {
      this.initializeCarousel();
    });
  },
  watch: {
    recentCharacters(newCharacters) {
      if (newCharacters.length > 0) {
        this.$nextTick(() => {
          this.initializeCarousel();
        });
      }
    },
  },
  methods: {
    initializeCarousel() {
      if ($(this.$el).find('.slick-carousel').hasClass('slick-initialized')) {
        $(this.$el).find('.slick-carousel').slick('unslick');
      }

      $(this.$el).find('.slick-carousel').slick({
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
      $(this.$el)
          .find('.slick-slide')
          .addClass('text-center slick-carousel-item');

    },
  },
};
</script>
