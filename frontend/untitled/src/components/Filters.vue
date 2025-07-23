<template>
  <div>
    <h4>Filtrovat</h4>
    <form @submit.prevent="applyFilters">
      <div class="form-group">
        <input
            class="form-control"
            v-model="localFilters.search"
            type="text"
            placeholder="Hledat podle jména/filmu"
        />
      </div>
      <div class="d-flex flex-wrap">
        <div class="col-12 col-sm-3 col-xl-12">
          <p class="mb-0 mt-2">Typ postav</p>
          <div class="form-check">
            <input
                class="form-check-input"
                type="checkbox"
                v-model="localFilters.showCartoon"
            />
            <label class="form-check-label">Animované postavy</label>
          </div>
          <div class="form-check">
            <input
                class="form-check-input"
                type="checkbox"
                v-model="localFilters.showIRL"
            />
            <label class="form-check-label">Hrané postavy</label>
          </div>
        </div>
        <div class="col-12 col-sm-3 col-xl-12">
          <p class="mb-0 mt-2">Pohlaví postav</p>
          <div class="form-check">
            <input
                class="form-check-input"
                type="checkbox"
                value="Muž"
                v-model="localFilters.genders"
            />
            <label class="form-check-label">Muž</label>
          </div>
          <div class="form-check">
            <input
                class="form-check-input"
                type="checkbox"
                value="Žena"
                v-model="localFilters.genders"
            />
            <label class="form-check-label">Žena</label>
          </div>
          <div class="form-check">
            <input
                class="form-check-input"
                type="checkbox"
                value="Jiné"
                v-model="localFilters.genders"
            />
            <label class="form-check-label">Jiné</label>
          </div>
        </div>
        <div class="col-12 col-sm-3 col-xl-12 mt-2">
          <select class="mb-2" v-model="localFilters.sortOrder">
            <option value="">Seřadit dle</option>
            <option value="name ASC">Jméno (A-Z)</option>
            <option value="name DESC">Jméno (Z-A)</option>
            <option value="date DESC">Datum přidání (nové-staré)</option>
            <option value="date ASC">Datum přidání (staré-nové)</option>
          </select>
        </div>
        <div>
          <button class="btn btn-primary" type="submit">Filtrovat</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      localFilters: {
        search: '',
        genders: [],
        showCartoon: true,
        showIRL: true,
        sortOrder: '',
      },
    };
  },
  watch: {
    // Sleduj URL parametry a aktualizuj localFilters
    '$route.query': {
      immediate: true,
      handler(query) {
        this.localFilters.search = query.search || '';
        this.localFilters.genders = query.genders
            ? Array.isArray(query.genders)
                ? query.genders
                : query.genders.split(',')
            : ['Muž', 'Žena', 'Jiné'];

        this.localFilters.showCartoon =
            query.showCartoon !== undefined ? query.showCartoon === 'true' : true;
        this.localFilters.showIRL =
            query.showIRL !== undefined ? query.showIRL === 'true' : true;

        this.localFilters.sortOrder = query.sortOrder || '';
      },
    },
  },
  methods: {
    applyFilters() {
      // Pokud není žádné pohlaví vybráno → nastav všechny
      if (this.localFilters.genders.length === 0) {
        this.localFilters.genders = ['Muž', 'Žena', 'Jiné'];
      }

      // Pokud není zvolen žádný typ postavy → nastav oba
      if (!this.localFilters.showCartoon && !this.localFilters.showIRL) {
        this.localFilters.showCartoon = true;
        this.localFilters.showIRL = true;
      }

      // Emitni filtry
      this.$emit('filters-changed', this.localFilters);
    },
  },
};
</script>
