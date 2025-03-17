<!-- RegisterForm.vue -->
<template>
  <div class="col-md-6">
    <h2 class="mt-4 mb-4">Zaregistruj se</h2>
    <p v-if="message" class="text-danger">{{ message }}</p>
    <form @submit.prevent="register">
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
        <label for="email">Email:</label>
        <input
            type="email"
            class="form-control"
            id="email"
            v-model="email"
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
      <button type="submit" class="btn btn-primary mt-3" :disabled="isLoading">
        <span v-if="isLoading" class="spinner-border spinner-border-sm me-2"></span>
        {{ isLoading ? 'Odesílání...' : 'Zaregistrovat' }}
      </button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      username: '',
      email: '',
      password: '',
      message: '',
      isLoading: false
    }
  },
  methods: {
    async register() {
      this.isLoading = true;
      this.message = '';
      try {
        const response = await axios.post('/api/users/register', {
          username: this.username,
          email: this.email,
          password: this.password
        });
        this.message = response.data;
        localStorage.setItem('tempPassword', this.password);
        this.$emit('otp-sent', this.email, this.username, this.password);
      } catch (err) {
        this.message = err.response?.data || 'Chyba při registraci';
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>
