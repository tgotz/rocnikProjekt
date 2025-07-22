<template>
  <div class="text-center database-item col-12 col-sm-6 col-lg-3">
    <router-link :to="`/detail/${character.id}`">
      <div class="m-2 character-item">
        <img class="character-image" :src="'data:image/jpeg;base64,' + character.imageBytes" :alt="character.name" />
        <div class="caption">
          <h5 class="my-0">{{ character.name }}</h5>
          <p class="my-0">{{ character.filmName }}</p>
        </div>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  props: {
    character: Object,
  },
};
</script>