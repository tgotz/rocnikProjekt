<template>
  <div>
    <div class="container mt-5 bg-main pt-2">
      <h2>Detaily postavy</h2>
      <hr />
      <!-- Basic info -->
      <CharacterDetail v-if="character" :character="character" />

      <!-- Quotes  -->
      <CharacterQuotes v-if="quotes.length" :quotes="quotes" />
      <hr />

      <!-- Form for reviews -->
      <ReviewForm
          v-if="character"
          :character-id="character.id"
          @review-submitted="fetchCharacterData"
      />
      <h3 class="mt-5">Recenze</h3>

      <!-- list of reviews -->
      <ReviewsList v-if="reviews.length" :reviews="reviews" />
      <p v-else class="text-center my-2">
        Nikdo zatím postavu neohodnotil, nechcete být první?
      </p>
    </div>
  </div>
</template>

<script>
import CharacterDetail from '../components/CharacterDetail.vue';
import CharacterQuotes from '../components/CharacterQuotes.vue';
import ReviewForm from '../components/ReviewForm.vue';
import ReviewsList from '../components/ReviewsList.vue';
import axios from 'axios';

export default {
  components: {
    CharacterDetail,
    CharacterQuotes,
    ReviewForm,
    ReviewsList,
  },
  data() {
    return {
      character: null,
      quotes: [],
      reviews: [],
    };
  },
  methods: {
    async fetchCharacterData() {
      try {
        const response = await axios.get(`http://localhost:8080/character`, {
          params: { id: this.$route.params.id },
        });

        this.character = response.data.character;
        this.quotes = response.data.quotes;
        this.reviews = response.data.reviews;
        console.log("Postava načtena:", this.character);
        console.log("Recenze při načtení:", this.reviews);

      } catch (error) {
        console.error('Error fetching character data:', error);
      }
    },
  },
  mounted() {
    this.fetchCharacterData();
  },
};
</script>
