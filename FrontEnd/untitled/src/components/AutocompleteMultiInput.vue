<template>
  <div class="form-group mb-2 position-relative">
    <label for="movieInput">Filmy *</label>
    <textarea
        id="movieInput"
        class="form-control"
        v-model="inputText"
        rows="4"
        placeholder="Zadejte název filmu a potvrďte Enterem. Každý řádek = jeden film."
        @input="filterSuggestions"
        @click="updateActiveLine"
        @keyup="updateActiveLine"
        @keydown.enter="handleEnter"
    />

    <!-- Suggestions -->
    <ul v-if="filteredSuggestions.length" class="suggestions-list">
      <li
          v-for="(suggestion, index) in filteredSuggestions"
          :key="index"
          @mousedown.prevent="selectSuggestion(suggestion.nameMovie)"
          class="text-dark"
      >
        {{ suggestion.nameMovie }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, defineExpose } from 'vue';
import axios from 'axios';


const props = defineProps(['modelValue']);
const inputText = ref(props.modelValue || '');
const emit = defineEmits(['update:modelValue']);
const activeLineIndex = ref(0);

const filteredSuggestions = ref([]);

watch(inputText, (val) => {
  emit('update:modelValue', val);
});
const updateActiveLine = (event) => {
  const position = event.target.selectionStart;
  const value = event.target.value;
  const lines = value.substring(0, position).split("\n");
  activeLineIndex.value = lines.length - 1;
};

const filterSuggestions = async () => {
  const lines = inputText.value.split("\n");
  const currentLine = lines[activeLineIndex.value]?.toLowerCase().trim();

  if (!currentLine || currentLine.length < 2) {
    filteredSuggestions.value = [];
    return;
  }

  try {
    const response = await axios.get(`http://localhost:8080/api/movies/search?query=${encodeURIComponent(currentLine)}`, {
      withCredentials: true,
    });
    filteredSuggestions.value = response.data;
  } catch (err) {
    console.error("Chyba při vyhledávání filmů:", err);
  }
};


const selectSuggestion = (movieName) => {
  const lines = inputText.value.split('\n');
  lines[activeLineIndex.value] = movieName;
  lines.push(""); // enter
  inputText.value = lines.join('\n');
  filteredSuggestions.value = [];
};

// Enter confirms movie even if it's not from suggestions
const handleEnter = () => {
  filteredSuggestions.value = []; // hide suggestions
};

// for parents
defineExpose({
  getMovies: () => {
    return inputText.value
        .split('\n')
        .map(line => line.trim())
        .filter(line => line.length > 0);
  },
  reset() {
    inputText.value = '';
  },
  setText(text) {
    inputText.value = text;
  }

});
</script>

<style scoped>
.suggestions-list {
  position: absolute;
  background: white;
  border: 1px solid #ccc;
  list-style: none;
  padding: 0;
  margin: 2px 0 0 0;
  width: 100%;
  max-height: 150px;
  overflow-y: auto;
  z-index: 1000;
}

.suggestions-list li {
  padding: 8px 12px;
  cursor: pointer;
}

.suggestions-list li:hover {
  background-color: #f0f0f0;
}
</style>
