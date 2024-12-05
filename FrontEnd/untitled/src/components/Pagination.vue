<template>
  <nav aria-label="Page navigation" class="my-5">
    <ul class="pagination justify-content-center mt-3">
      <!-- Předchozí stránka -->
      <li class="page-item" :class="{ disabled: currentPage === 1 }">
        <button
            class="page-link"
            @click="changePage(currentPage - 1)"
            :disabled="currentPage === 1"
        >
          Předchozí
        </button>
      </li>

      <!-- Dvě předchozí stránky -->
      <li
          v-if="currentPage > 2"
          class="page-item"
      >
        <button
            class="page-link"
            @click="changePage(currentPage - 2)"
        >
          {{ currentPage - 2 }}
        </button>
      </li>
      <li
          v-if="currentPage > 1"
          class="page-item"
      >
        <button
            class="page-link"
            @click="changePage(currentPage - 1)"
        >
          {{ currentPage - 1 }}
        </button>
      </li>

      <!-- Aktuální stránka -->
      <li class="page-item active">
        <button class="page-link">
          {{ currentPage }}
        </button>
      </li>

      <!-- Dvě následující stránky -->
      <li
          v-if="currentPage < maxPage"
          class="page-item"
      >
        <button
            class="page-link"
            @click="changePage(currentPage + 1)"
        >
          {{ currentPage + 1 }}
        </button>
      </li>
      <li
          v-if="currentPage < maxPage - 1"
          class="page-item"
      >
        <button
            class="page-link"
            @click="changePage(currentPage + 2)"
        >
          {{ currentPage + 2 }}
        </button>
      </li>

      <!-- Další stránka -->
      <li class="page-item" :class="{ disabled: currentPage === maxPage }">
        <button
            class="page-link"
            @click="changePage(currentPage + 1)"
            :disabled="currentPage === maxPage"
        >
          Další
        </button>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  props: {
    currentPage: { type: Number, required: true }, // Aktuální stránka
    maxPage: { type: Number, required: true }, // Celkový počet stránek
  },
  methods: {
    changePage(newPage) {
      if (newPage > 0 && newPage <= this.maxPage) {
        this.$emit('page-changed', newPage); // Emituj změnu rodiči
        this.$router.push({ query: { ...this.$route.query, page: newPage } }); // Aktualizuj URL
      }
    },
  },
};
</script>
