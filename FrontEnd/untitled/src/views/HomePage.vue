<template>
  <div class="container">
    <Carousel v-if="characters.length > 0" :characters="characters" />
    <div class="row">
      <h4 id="database" class="text-center fs-2 my-5">Databáze postav</h4>
      <div class="col-xl-3">
        <Filters
            :filters="filters"
            @filters-changed="onFiltersChanged"
        />      </div>
      <div class="col-xl-9" id="database">
        <CharacterList :characters="filteredCharacters" />
      </div>
    </div>
    <Pagination
        :current-page="currentPage"
        :max-page="totalPages"
        @page-changed="changePage"
    />
  </div>
</template>

<script>
import axios from 'axios';
import Filters from '../components/Filters.vue';
import CharacterList from '../components/CharacterList.vue';
import Pagination from '../components/Pagination.vue';
import Carousel from '../components/Carousel.vue';

export default {
  components: { Carousel, Filters, CharacterList, Pagination },
  data() {
    return {
      characters: [],
      filteredCharacters: [],
      filters: {
        search: '',
        showCartoon: true,
        showIRL: true,
        genders: ['Muž', 'Žena', 'Jiné'],
        sortOrder: '',
      },
      currentPage: 1,
      itemsPerPage: 24,
      totalPages: 1,
    };
  },
  methods: {
    async fetchCharacters() {
      try {
        const response = await axios.get('http://localhost:8080/api/character');
        this.characters = response.data;
        this.filterCharacters();
      } catch (error) {
        console.error('Error fetching characters:', error);
      }
    },

    filterCharacters() {
      const { search, showCartoon, showIRL, genders, sortOrder } = this.filters;

      let filtered = this.characters;

      if (search) {
        filtered = filtered.filter((character) =>
            character.name.toLowerCase().includes(search.toLowerCase()) ||
            (character.movies?.some(movie => movie.nameMovie.toLowerCase().includes(search.toLowerCase())) ?? false)
        );
      }

      filtered = filtered.filter((character) => {
        if (!showCartoon && character.type === 'Animovaná') return false;
        if (!showIRL && character.type === 'Hraná') return false;
        return true;
      });

      if (genders.length > 0) {
        filtered = filtered.filter((character) =>
            genders.includes(character.gender)
        );
      }

      if (sortOrder === 'name ASC') {
        filtered.sort((a, b) => a.name.localeCompare(b.name));
      } else if (sortOrder === 'name DESC') {
        filtered.sort((a, b) => b.name.localeCompare(a.name));
      } else if (sortOrder === 'date DESC') {
        filtered.sort((a, b) => new Date(b.dateAdded) - new Date(a.dateAdded));
      } else if (sortOrder === 'date ASC') {
        filtered.sort((a, b) => new Date(a.dateAdded) - new Date(b.dateAdded));
      }

      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      this.filteredCharacters = filtered.slice(start, end);
      this.totalPages = Math.ceil(filtered.length / this.itemsPerPage);
    },

    changePage(newPage) {
      if (newPage > 0 && newPage <= this.totalPages) {
        this.currentPage = newPage;
        this.updateURL();
        this.filterCharacters();
      }
    },

    onFiltersChanged(newFilters) {
      this.filters = newFilters;
      this.currentPage = 1;
      this.updateURL();
      this.filterCharacters();
    },

    updateURL() {
      const query = {
        page: this.currentPage,
        search: this.filters.search || undefined,
        showCartoon: this.filters.showCartoon,
        showIRL: this.filters.showIRL,
        genders: this.filters.genders.join(','),
        sortOrder: this.filters.sortOrder || undefined,
      };
      this.$router.push({ query });
    },

    loadFromURL() {
      const query = this.$route.query;
      this.currentPage = parseInt(query.page) || 1;
      this.filters = {
        search: query.search || '',
        showCartoon: query.showCartoon === 'true',
        showIRL: query.showIRL === 'true',
        genders: query.genders ? query.genders.split(',') : ['Muž', 'Žena', 'Jiné'],
        sortOrder: query.sortOrder || '',
      };
    },
  },

  mounted() {
    this.loadFromURL();
    this.fetchCharacters();
  },
};
</script>
