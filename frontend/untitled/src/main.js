import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js'; // Import routeru
import './assets/styles/bootstrap.css';
import './assets/styles/styles.css';
import 'animate.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';


import axios from 'axios';
import createAppRouter from "./router";
import {createPinia} from "pinia";
axios.defaults.withCredentials = true;
axios.defaults.baseURL = "http://localhost:8080";
(async () => {
    const app = createApp(App);
    app.config.devtools = false;
    const pinia = createPinia(); // Pinia - for user management
    app.use(pinia);

    const router = await createAppRouter(pinia);
    app.use(router);

    app.mount('#app');
})();
