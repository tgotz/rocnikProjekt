<template>
  <div>
    <h3>Hlášky</h3>
    <ul>
      <li v-for="(quote, index) in quotes" :key="index">{{ quote }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    quotes: {
      type: Array,
      required: true,
    },
  },
};
</script>
