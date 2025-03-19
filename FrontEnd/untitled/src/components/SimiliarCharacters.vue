<template>
  <div class="d-none d-lg-block" v-if="similarCharacters.length > 0" >
    <h3>Mohlo by se vám líbit:</h3>
    <div class="similar-characters row ">
      <CharacterCard
          v-for="character in similarCharacters"
          :key="character.id"
          :character="character"
      />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import CharacterCard from "@/components/CharacterCard.vue";

export default {
  components: {CharacterCard},
  props: {
    characterId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      similarCharacters: [],
    };
  },
  methods: {
    async fetchSimilarCharacters() {
      try {
        const response = await axios.get(
            `http://localhost:8080/api/character/${this.characterId}/similar`
        );
        this.similarCharacters = response.data;
      } catch (error) {
        console.error("Chyba při načítání podobných postav:", error);
      }
    },
  },
  mounted() {
    this.fetchSimilarCharacters();
  },
};
</script>

<style scoped>

</style>
