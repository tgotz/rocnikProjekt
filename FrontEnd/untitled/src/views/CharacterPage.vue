<template>
  <div>
    <div class="container mt-5 bg-main pt-2">
      <h2>Detaily postavy</h2>
      <hr />
      <!-- Základní informace o postavě -->
      <CharacterDetail v-if="character" :character="character" />

      <!-- Hlášky -->
      <CharacterQuotes v-if="quotes.length" :quotes="quotes" />
      <hr />

      <!-- Formulář pro přidání recenze -->
      <ReviewForm
          v-if="character"
          :character-id="character.id"
          @review-submitted="fetchReviews"
      />
      <h3 class="mt-5">Recenze</h3>

      <!-- Seznam recenzí -->
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
      } catch (error) {
        console.error('Error fetching character data:', error);
      }
    },
    async fetchReviews() {
      // Použito po odeslání recenze, aby se seznam recenzí aktualizoval
      try {
        const response = await axios.get(`http://localhost:8080/reviews`, {
          params: { characterId: this.character.id },
        });
        this.reviews = response.data.reviews;
      } catch (error) {
        console.error('Error fetching reviews:', error);
      }
    },
  },
  mounted() {
    this.fetchCharacterData();
  },
};
</script>
