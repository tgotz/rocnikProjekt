<template>
  <div>
    <button
        @click="requestOtp"
        class="btn btn-primary"
        :disabled="isButtonDisabled"
    >
      {{ otpSent ? `Znovu odeslat OTP${countdown > 0 ? ` (${countdown}s)` : ''}` : "Změnit heslo" }}
    </button>

    <div v-if="otpSent" class="mt-3">
      <div class="form-group">
        <label>Staré heslo</label>
        <input v-model="oldPassword" type="password" class="form-control" />
      </div>
      <div class="form-group">
        <label>OTP z e-mailu</label>
        <input v-model="otp" type="text" class="form-control" />
      </div>
      <div class="form-group">
        <label>Nové heslo</label>
        <input v-model="newPassword" type="password" class="form-control" />
      </div>
      <button @click="submitChange" class="btn btn-success mt-2">Potvrdit změnu</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
});

const oldPassword = ref("");
const newPassword = ref("");
const otp = ref("");
const otpSent = ref(false);
const isButtonDisabled = ref(false);
const countdown = ref(0);
let countdownInterval = null;

const startCountdown = (duration = 30) => {
  countdown.value = duration;
  isButtonDisabled.value = true;

  countdownInterval = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(countdownInterval);
      isButtonDisabled.value = false;
    }
  }, 1000);
};

const requestOtp = async () => {
  isButtonDisabled.value = true;
  try {
    await axios.post(`${import.meta.env.VITE_API_URL}/api/users/send-otp`, {
      email: props.user.email
    }, { withCredentials: true });

    otpSent.value = true;
    startCountdown();
  } catch (error) {
    console.error("Chyba při odesílání OTP:", error);
    alert("Nepodařilo se odeslat OTP.");
    isButtonDisabled.value = false;
  }
};

const submitChange = async () => {
  try {
    await axios.post(`${import.meta.env.VITE_API_URL}/api/users/change-password`, {
      oldPassword: oldPassword.value,
      newPassword: newPassword.value,
      otp: otp.value,
    }, { withCredentials: true });

    alert("Heslo bylo úspěšně změněno!");
    oldPassword.value = "";
    newPassword.value = "";
    otp.value = "";
    otpSent.value = false;
    clearInterval(countdownInterval);
    countdown.value = 0;
    isButtonDisabled.value = false;
  } catch (error) {
    console.error("Chyba při změně hesla:", error);
    alert("Změna hesla selhala. Zkontrolujte OTP nebo staré heslo.");
  }
};
</script>
