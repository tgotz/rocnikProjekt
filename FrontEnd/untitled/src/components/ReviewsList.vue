<template>
  <div>
    <ul class="list-group">
      <li class="list-group-item my-3" v-for="(review, index) in reviews" :key="index">
        <strong>{{ review.authorName }} říká:</strong><br />
        <strong>Celkové hodnocení: {{ review.overallRating }}/10</strong><br />
        <strong>Hodnocení atraktivity: {{ review.attractivenessRating }}/10</strong><br />
        <strong>{{ review.reviewText }}</strong>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  props: {
    reviews: {
      type: Array,
      required: true,
    },
  },
};
</script>
