<template>
  <div class="d-flex flex-wrap" id="characters">
    <CharacterCard
        v-for="character in characters"
        :key="character.id"
        :character="character"
    />
  </div>
</template>

<script>
import CharacterCard from './CharacterCard.vue';

export default {
  components: { CharacterCard },
  props: {
    characters: Array,
  },
};
</script>
