<template>
  <div>
    <input
        v-model="query"
        @input="fetchSuggestions"
        type="text"
        class="form-control"
        :placeholder="placeholder"
    />
    <div v-if="suggestions.length" class="suggestions">
      <ul>
        <li
            v-for="suggestion in suggestions"
            :key="suggestion"
            @click="selectSuggestion(suggestion)"
        >
          {{ suggestion }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    placeholder: {
      type: String,
      default: 'Zadej jméno filmu/seriál',
    },
  },
  data() {
    return {
      query: '',
      suggestions: [],
    };
  },
  methods: {
    async fetchSuggestions() {
      if (this.query.length < 3) return;
      try {
        const response = await fetch(`http://localhost:8080/FilmSuggestions?q=${this.query}`);
        this.suggestions = await response.json();
      } catch (error) {
        console.error('Chyba při načítání návrhů:', error);
      }
    },
    selectSuggestion(suggestion) {
      this.query = suggestion;
      this.$emit('update:modelValue', suggestion);
      this.suggestions = [];
    },
  },
};
</script>
