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
          <div class=" mt-2 d-flex justify-content-between align-items-center">
            <button type="submit" class="btn btn-primary">Přihlásit</button>
            <router-link class="" aria-current="page" to="/register">Nemáte ještě účet? Zaregistrujte se</router-link>

          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from "../stores/userStore";
import axios from "axios";

export default {
  data() {
    return {
      username: "",
      password: "",
      role: "",
      errorMessage: null,
    };
  },
  methods: {
    async login() {
      const userStore = useUserStore();

      try {
        const response = await axios.post(`${import.meta.env.VITE_API_URL}/api/auth/login`, {
          username: this.username,
          password: this.password,
        });

        const { token, username, role, userId } = response.data;
        localStorage.setItem("jwt", token);

        await userStore.setUser({
          username: response.data.username,
          role: parseInt(response.data.role),
          userId: parseInt(response.data.userId),
        });

        await this.checkLoginStatus();

        this.$router.push("/");

      } catch (error) {
        if (error.response?.status === 403 && error.response?.data?.error === 'NOT_VERIFIED') {
          localStorage.setItem('tempPassword', this.password);
          this.$router.push({
            path: '/register',
            query: {otp: 'true',  email: error.response.data.email, username: error.response.data.username, autoSendOtp: 'true' }
          });
        } else if (error.response && error.response.status === 401) {
          this.errorMessage = "Nesprávné heslo nebo přihlašovací jméno.";
        } else {
          this.errorMessage = "Nastala chyba. Zkuste to prosím později.";
        }
      }
    },

    async checkLoginStatus() {
      const userStore = useUserStore();
      try {
        const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/users/user-info`, {
          withCredentials: true,
        });

        if (response.data.username) {
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
