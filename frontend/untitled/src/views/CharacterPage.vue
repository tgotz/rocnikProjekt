<template>
  <div>
    <div class="container mt-5 bg-main pt-2">
      <div class="d-flex justify-content-between">
        <h2>Detaily postavy</h2>
        <div v-if="user && character" class="d-flex align-items-center">
          <ReportPopup
              v-if="character?.id && user?.userId"
              :type="'character'"
              :targetId="character.id"
              :userId="user.userId"
          />
            <div class="ms-3" v-if="user?.role && user.role >= 3 && character?.id">
            <a :href="`/edit-character/${character.id}`" class=" text-decoration-none me-3" target="_blank">Upravit</a>
            <a href="#" class=" text-decoration-none" @click.prevent="deleteCharacter(character.id)">Smazat</a>
          </div>
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
      <ReviewsList v-if="reviews.length"
                   :reviews="reviews"
                   :user="user"
                   @delete-review="deleteReview"
      />
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
import ReportPopup from "@/components/ReportPopup.vue";


export default {
  components: {
    ReportPopup,
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
              `${import.meta.env.VITE_API_URL}/api/character/delete-character/${id}`,
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
        const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/character/${this.$route.params.id}`);

        this.character = response.data.character;
        this.quotes = response.data.quotes;
        this.reviews = response.data.reviews;
        console.log("Postava načtena:", this.character);
        console.log("Recenze při načtení:", this.reviews);
        console.log(this.user)
      } catch (error) {
        console.error('Error fetching character data:', error);
      }
    },
    async deleteReview(reviewId) {
      console.log("sem tady 2")
      if (!confirm("Opravdu chcete smazat tuto recenzi?")) return;

      try {
        await axios.delete(`${import.meta.env.VITE_API_URL}/api/reviews/${reviewId}`, {
          withCredentials: true
        });
        this.reviews = this.reviews.filter(r => r.id !== reviewId);
        alert("Recenze byla smazána.");
      } catch (error) {
        console.error("Chyba při mazání recenze:", error);
        alert("Při mazání došlo k chybě.");
      }
    }
  },
  mounted() {
    this.fetchCharacterData();
  },
};
</script>
