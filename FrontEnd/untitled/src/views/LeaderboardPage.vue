<template>
  <div class="container mt-5">
    <h2 class="mb-5 outline-heading text-center">{{ headline }}</h2>
    <div class="bg-main p-2">
      <table class="table table-striped">
        <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Jméno</th>
          <th class="d-none d-lg-table-cell" scope="col">Film</th>
          <th class="d-none d-lg-table-cell" scope="col">Herec/Dabér</th>
          <th scope="col">Celkové hodnocení</th>
          <th scope="col">Hodnocení atraktivity</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(character, index) in characters" :key="character.id">
          <th scope="row">{{ index + 1 }}.</th>
          <td>
            <router-link class="text-decoration-none text-dark"
                :to="{ name: 'Character', params: { id: character.id } }"
            >
              {{ character.name }}
            </router-link>
          </td>
          <td class="d-none d-lg-table-cell">{{ character.movies[0].nameMovie }}</td>
          <td class="d-none d-lg-table-cell">
            {{
              character.actorName && character.dabberName
                  ? character.actorName + ' / ' + character.dabberName
                  : (character.actorName || character.dabberName)
            }}
          </td>          <td>{{ character.overall.toFixed(1) }}</td>
          <td>{{ character.attractiveness.toFixed(1) }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "LeaderboardPage",
  data() {
    return {
      characters: [],
      headline: "",
    };
  },
  watch: {
    "$route.query.sort": "fetchLeaderboard",
  },
  methods: {
    async fetchLeaderboard() {
      try {
        const sort = this.$route.query.sort || "overall DESC";
        const response = await axios.get("http://localhost:8080/api/leaderboard", {
          params: { sort },
        });

        this.characters = response.data;
      } catch (error) {
        console.error("Chyba při načítání leaderboardu:", error);
      }

  },
  },
  created() {
    this.fetchLeaderboard();
  },
};
</script>