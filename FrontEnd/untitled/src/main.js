import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // Import routeru
import './assets/styles/bootstrap.css';
import './assets/styles/styles.css';
import 'animate.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';

import axios from 'axios';
axios.defaults.withCredentials = true;
axios.defaults.baseURL = "http://localhost:8080";

import { createPinia } from "pinia";

const pinia = createPinia();

const app = createApp(App);
app.use(pinia);
app.use(router); // Použití routeru
app.mount('#app');

