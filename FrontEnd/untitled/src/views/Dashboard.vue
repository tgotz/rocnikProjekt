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
        <td class="d-none d-md-table-cell">{{ character.approvedBy }}</td>
        <td class="d-none d-md-table-cell">{{ character.addedBy ?? "Nepřihlášený uživatel" }}</td>
        <td class="d-none d-lg-table-cell">{{ character.movieList[0] }}</td>
        <td class="d-none d-lg-table-cell">
          {{
            character.actorName && character.dabberName
                ? character.actorName + ' / ' + character.dabberName
                : (character.actorName || character.dabberName)
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

        // Sloučení všech relevantních hodnot do jednoho stringu
        const characterData = [
          character.name,
          character.filmName,
          character.actorName,
          character.dabberName,

          ...(Array.isArray(character.movieList) ? character.movieList : []) // Ověříme, že `movieList` je pole
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
        const response = await axios.get("http://localhost:8080/api/dashboard", {
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
          await axios.post(`/api/delete-character?id=${id}`, {
            withCredentials: true,
          });
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
