<template>
  <div class="container">
    <div class="row justify-content-center">
      <RegisterForm v-if="!otpSent" @otp-sent="onOtpSent" />
      <OtpVerificationForm
          v-else
          :email="email"
          :username="username"
          :password="password"
          :autoSendOtp="autoSendOtp"
      />
    </div>
  </div>
</template>

<script>

import RegisterForm from '../components/RegisterForm.vue'
import OtpVerificationForm from '../components/OtpVerficationForm.vue'
import {useUserStore} from "@/stores/userStore.js";
import axios from "axios";

export default {
  components: { RegisterForm, OtpVerificationForm },
  data() {
    return {
      otpSent: false,
      email: '',
      username: '',
      password: '',
      autoSendOtp: false
    }
  },
  created() {
    // Pokud se přišlo z loginu s query parametry
    if (this.$route.query.otp === 'true' && this.$route.query.email) {
      this.email = this.$route.query.email;
      this.username = this.$route.query.username || '';
      this.password = localStorage.getItem('tempPassword') || '';
      this.autoSendOtp = this.$route.query.autoSendOtp === 'true';
      this.otpSent = true; // 🔥 až pak otpSent
    }
  },
  methods: {
    onOtpSent(email, username, password) {
      this.email = email;
      this.username = username;
      this.password = password;
      this.otpSent = true;
    },
  }
}
</script>
