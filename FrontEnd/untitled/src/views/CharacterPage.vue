<template>
  <div>
    <div class="container mt-5 bg-main pt-2">
      <div class="d-flex justify-content-between">
        <h2>Detaily postavy</h2>
        <div v-if="user?.role && user.role >= 3 && character?.id">
          <a :href="`/edit-character/${character.id}`" class=" text-decoration-none me-3" target="_blank">Upravit</a>
          <a href="#" class=" text-decoration-none" @click.prevent="deleteCharacter(character.id)">Smazat</a>
        </div>
      </div>
      <hr />
      <!-- Basic info -->
      <CharacterDetail v-if="character" :character="character" />

      <!-- Quotes  -->
      <CharacterQuotes v-if="quotes.length" :quotes="quotes" />
      <hr />

      <SimiliarCharacters          v-if="character" :characterId="character.id" />

      <!-- Form for reviews -->
      <ReviewForm
          v-if="character"
          :character-id="character.id"
          @review-submitted="fetchCharacterData"
      />
      <h3 class="mt-5">Recenze</h3>

      <!-- list of reviews -->
      <ReviewsList v-if="reviews.length" :reviews="reviews" />
      <p v-else class="text-center mt-2 mb-5">
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
import SimiliarCharacters from "@/components/SimiliarCharacters.vue";
import { useUserStore } from "../stores/userStore";

export default {
  components: {
    SimiliarCharacters,
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
  computed: {
    user() {
      const userStore = useUserStore();
      return userStore.user;  // přístup k uživatelským datům
    }
  },
  methods: {
    async deleteCharacter(id) {
      if (confirm("Opravdu chcete smazat tuto postavu?")) {
        try {
          await axios.post(
              `http://localhost:8080/api/character/delete-character/${id}`,
              {},
              { withCredentials: true }
          );
          this.$router.push('/');
        } catch (error) {
          console.error("Chyba při mazání postavy:", error);
        }
      }
    },
    async fetchCharacterData() {
      try {
        const response = await axios.get(`http://localhost:8080/api/character/${this.$route.params.id}`);

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
