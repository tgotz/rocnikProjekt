<template>
  <div class="autocomplete-wrapper position-relative">
    <input
        v-model="input"
        @keyup="onKeyup"
        type="text"
        class="form-control"
        :placeholder="placeholder"
        :required="required"
    />
    <ul v-if="suggestions.length && showSuggestions" class="autocomplete-list">
      <li
          v-for="(item, index) in suggestions"
          :key="index"
          @click="select(item.name)"
          class="text-dark"
      >
        {{ item.name }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, defineEmits, defineProps } from "vue";
import axios from "axios";

const props = defineProps({
  modelValue: String,
  searchUrl: String,
  placeholder: String,
  required: Boolean
});

const emit = defineEmits(["update:modelValue"]);

const input = ref(props.modelValue || "");
const suggestions = ref([]);
const showSuggestions = ref(false);

watch(() => props.modelValue, (val) => {
  input.value = val;
});

const onKeyup = (event) => {
  const value = event.target.value;
  input.value = value;
  emit("update:modelValue", value);
  filterSuggestions(value);
};

const filterSuggestions = async (value) => {
  if (!value || value.length < 2) {
    suggestions.value = [];
    return;
  }

  try {
    const res = await axios.get(props.searchUrl, {
      params: { query: value },
      withCredentials: true,
    });
    suggestions.value = res.data;
    showSuggestions.value = true;
  } catch (err) {
    console.error("Autocomplete error:", err);
  }
};


const select = (item) => {
  input.value = item;
  emit("update:modelValue", item);
  suggestions.value = [];
  showSuggestions.value = false;
};
</script>

<style scoped>
.autocomplete-list {
  position: absolute;
  background: white;
  border: 1px solid #ccc;
  z-index: 1000;
  width: 100%;
  padding: 0;
  margin: 0.25rem 0 0 0;
  list-style: none;
  max-height: 200px;
  overflow-y: auto;
}

.autocomplete-list li {
  padding: 0.5rem;
  cursor: pointer;
}

.autocomplete-list li:hover {
  background-color: #f0f0f0;
}
</style>
