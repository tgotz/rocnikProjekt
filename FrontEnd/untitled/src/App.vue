<template>
  <div id="app">
    <Header />

    <router-view :key="$route.fullPath"></router-view>
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


