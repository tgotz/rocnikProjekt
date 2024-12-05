<template>
  <div id="app">
    <!-- Hlavní navigace -->
    <Header />

    <!-- Router View: vykresluje stránku podle aktuální URL -->
    <router-view />
  </div>
</template>

<script>
import Header from './components/Header.vue';

export default {
  components: {
    Header,
  },
};
</script>


