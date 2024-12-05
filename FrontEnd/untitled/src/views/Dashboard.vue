<template>
  <div class="container mt-5 bg-main pt-2">
    <!-- Vyhledávací pole -->
    <input
        v-model="searchQuery"
        type="text"
        class="form-control my-3"
        placeholder="Vyhledat postavu, film nebo herce"
    />

    <!-- Tabulka postav -->
    <table class="table table-striped mb-3">
      <thead>
      <tr>
        <th>#</th>
        <th>Jméno</th>
        <th>Schválil</th>
        <th class="d-none d-lg-table-cell">Film</th>
        <th class="d-none d-lg-table-cell">Herec</th>
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
        <td>{{ character.adminName }}</td>
        <td class="d-none d-lg-table-cell">{{ character.filmName }}</td>
        <td class="d-none d-lg-table-cell">{{ character.actorName }}</td>
        <td>
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
      characters: [], // Seznam všech postav
      searchQuery: "", // Hledaný text
    };
  },
  computed: {
    // Filtrované postavy na základě vyhledávání
    filteredCharacters() {
      return this.characters.filter((character) =>
          [character.name, character.filmName, character.actorName]
              .join(" ")
              .toLowerCase()
              .includes(this.searchQuery.toLowerCase())
      );
    },
  },
  methods: {
    // Načtení seznamu postav z backendu
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
    // Smazání postavy
    async deleteCharacter(id) {
      if (confirm("Opravdu chcete smazat tuto postavu?")) {
        try {
          await axios.delete(`/api/delete-character?id=${id}`, {
            withCredentials: true,
          });
          // Aktualizace seznamu postav po smazání
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
