<template>
  <div class="container mt-5 bg-main pt-2">
    <input
        v-model="searchQuery"
        type="text"
        class="form-control my-3"
        placeholder="Vyhledat postavu, film nebo herce"
    />

    <!-- Table of characters -->
    <table class="table table-striped mb-3">
      <thead>
      <tr>
        <th>#</th>
        <th>Jméno</th>
        <th class="d-none d-md-table-cell">Schválil</th>
        <th class="d-none d-md-table-cell">Přidal</th>
        <th class="d-none d-lg-table-cell">Film</th>
        <th class="d-none d-lg-table-cell">Herec/Dabér</th>
        <th></th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(character, index) in filteredCharacters" :key="character.id">
        <td>{{ index + 1 }}</td>
        <td>
          <a :href="`/detail/${character.id}`" class="text-dark text-decoration-none"  target="_blank">{{ character.name }}</a>
        </td>
        <td class="d-none d-md-table-cell">{{ character.approvedBy.username }}</td>
        <td class="d-none d-md-table-cell">  {{ character.addedBy ? character.addedBy.username : "Nepřihlášený uživatel" }}</td>
        <td class="d-none d-lg-table-cell">{{ character.movies[0].nameMovie }}</td>
        <td class="d-none d-lg-table-cell">
          {{
            character.actor  && character.dabber
                ? character.actor.name + ' / ' + character.dabber.name
                : (character.actor?.name || character.dabber?.name || "")
          }}
        </td>        <td>
          <a :href="`/edit-character/${character.id}`" class="text-dark text-decoration-none"  target="_blank">Upravit</a>
        </td>
        <td>
          <a href="#" class="text-dark text-decoration-none"  @click.prevent="deleteCharacter(character.id)">Smazat</a>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      characters: [],
      searchQuery: "",
    };
  },
  computed: {
    // filteted characters based on search query
    filteredCharacters() {
      return this.characters.filter((character) => {
        const searchText = this.searchQuery.toLowerCase();

        // Bezpečné načtení hodnot (i při null)
        const actorName = character.actor?.name || "";
        const dabberName = character.dabber?.name || "";

        const movieNames = Array.isArray(character.movies)
            ? character.movies.map(movie => movie.nameMovie || "").join(" ")
            : "";

        const characterData = [
          character.name || "",
          actorName,
          dabberName,
          movieNames
        ]
            .join(" ")
            .toLowerCase();

        return characterData.includes(searchText);
      });
    }

  },
  methods: {
    async fetchCharacters() {
      try {
        const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/character/dashboard`, {
          withCredentials: true,
        });
        this.characters = response.data;
      } catch (error) {
        console.error("Chyba při načítání postav:", error);
      }
    },

    async deleteCharacter(id) {
      if (confirm("Opravdu chcete smazat tuto postavu?")) {
        try {
          await axios.delete(
              `${import.meta.env.VITE_API_URL}/api/character/delete-character/${id}`,
              { withCredentials: true }
          );
          // updating list of characters
          this.characters = this.characters.filter((character) => character.id !== id);
        } catch (error) {
          console.error("Chyba při mazání postavy:", error);
        }
      }
    },
  },
  mounted() {
    this.fetchCharacters();
  },
};
</script>
