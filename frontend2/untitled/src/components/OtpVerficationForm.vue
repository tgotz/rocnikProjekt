<!-- OtpVerificationForm.vue -->
<template>
  <div class="col-md-6">
    <h2 class="mt-4 mb-4">Ověř email</h2>
    <p v-if="message" >{{ message }}</p>
    <form @submit.prevent="verifyOtp">
      <div class="form-group">
        <label for="username">Ověřovací kód byl odeslán na {{this.email}}</label>
        <input
            type="text"
            class="form-control"
            id="otpCode"
            v-model="otpCode"
            required
        />
      </div>
      <button type="submit" class="btn btn-primary mt-3">Ověřit</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'
import {useUserStore} from "@/stores/userStore.js";

export default {
  props: {
    email: String,
    username: String,
    password: String,
    autoSendOtp: {
      type: Boolean,
      default: false
    }
  },  data() {
    return {
      otpCode: '',
      message: ''
    }
  },
  created() {
    console.log(this.autoSendOtp)
    if (this.autoSendOtp) {
      this.sendOtp();
    }
  },
  destroyed() {
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  },

  methods: {
    handleBeforeUnload() {
      localStorage.removeItem('tempPassword');
    },
    async sendOtp() {
      try {
        const response = await axios.post('/api/users/resend-otp', {
          email: this.email
        });
        this.message = response.data.message || 'Kód byl znovu odeslán';
      } catch (err) {
        this.message = err.response?.data || 'Chyba při opětovném odeslání kódu';
      }
    },
    async verifyOtp() {
      try {
        const response = await axios.post('/api/users/verify-otp', {
          email: this.email,
          otpCode: this.otpCode
        });
        this.message = response.data;
        await this.loginAfterVerification();
      } catch (err) {
        this.message = err.response?.data || 'Chyba při ověření';
      }
    },
    async loginAfterVerification() {
      const password = localStorage.getItem('tempPassword'); // Načteme heslo z localStorage
      try {
        const response = await axios.post('/api/auth/login', {
          username: this.username,
          password: password
        }, { withCredentials: true });

        const { token, username, role, userId } = response.data;
        localStorage.setItem("jwt", token);
        this.checkLoginStatus();
        this.$router.push('/dashboard');
        localStorage.removeItem('tempPassword');
      } catch (err) {
        console.error('Chyba při automatickém loginu:', err);
        localStorage.removeItem('tempPassword');
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

  }

}
</script>
