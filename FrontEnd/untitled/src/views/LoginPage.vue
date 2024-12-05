<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <h2 class="mt-4 mb-4">Přihlaš se</h2>
        <p v-if="errorMessage" class="text-danger">{{ errorMessage }}</p>
        <form @submit.prevent="login">
          <div class="form-group">
            <label for="username">Přihlašovací jméno:</label>
            <input
                type="text"
                class="form-control"
                id="username"
                v-model="username"
                required
            />
          </div>
          <div class="form-group">
            <label for="password">Heslo:</label>
            <input
                type="password"
                class="form-control"
                id="password"
                v-model="password"
                required
            />
          </div>
          <button type="submit" class="btn btn-primary mt-3">Přihlásit</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "../stores/userStore"; // Import Pinia store
import axios from "axios";

export default {
  data() {
    return {
      username: "",
      password: "",
      role: "",
      errorMessage: null, // Pro zobrazení chybové zprávy
    };
  },
  methods: {
    async login() {
      const userStore = useUserStore(); // Přístup k Pinia userStore

      try {
        const response = await axios.post("http://localhost:8080/login", {
          username: this.username,
          password: this.password,
        });

        // Uložení uživatele do Pinia
        await userStore.setUser({
          username: response.data.username,
          role: parseInt(response.data.role), // Přesný datový typ
          userId: parseInt(response.data.userId), // Přesný datový typ
        });

        // Kontrola stavu přihlášení (pro synchronizaci dat)
        await this.checkLoginStatus();

        // Přesměrování na domovskou stránku
        this.$router.push("/");

      } catch (error) {
        if (error.response && error.response.status === 401) {
          this.errorMessage = "Nesprávné heslo nebo přihlašovací jméno.";
        } else {
          this.errorMessage = "Nastala chyba. Zkuste to prosím později.";
        }
      }
    },

    async checkLoginStatus() {
      const userStore = useUserStore(); // Přístup k Pinia store
      try {
        const response = await axios.get("http://localhost:8080/user-info", {
          withCredentials: true, // Zahrnuje cookies
        });

        if (response.data.username) {
          // Aktualizace uživatele ve store
          userStore.setUser({
            username: response.data.username,
            role: parseInt(response.data.role),
            userId: parseInt(response.data.userId),
          });
        } else {
          userStore.clearUser();
        }
      } catch (error) {
        console.error("Chyba při ověřování přihlášení:", error);
        userStore.clearUser();
      }
    },
  },
};
</script>
