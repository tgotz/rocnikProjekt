<template>
  <div class="row">
    <div class="col-md-8">
      <h3>Jméno: {{ character.name }}</h3>
      <p v-if="character.nickname">Přezdívka: {{ character.nickname }}</p>
      <p v-if="character.age">Věk: {{ character.age }}</p>
      <p>Pohlaví: {{ character.gender }}</p>
      <p>Typ: {{ character.type }}</p>
      <p v-if="character.actor">Herec: {{ character.actor.name }}</p>
      <p v-if="character.dabber">Dabér: {{ character.dabber.name }}</p>
      <p>Filmy / Seriály</p>
      <ul>
        <li v-for="(movie, index) in character.movies" :key="index">
          {{ movie.nameMovie }}
        </li>
      </ul>
      <p>{{ character.description }}</p>
    </div>
    <div class="col-md-4">
      <img
          :src="'data:image/jpeg;base64,' + character.imageBytes"
          class="img-fluid"
          alt="Character Image"
      />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    character: {
      type: Object,
      required: true,
    },
  },
};
</script>
