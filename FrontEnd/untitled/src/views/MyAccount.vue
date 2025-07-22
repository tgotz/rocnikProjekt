<template>
  <div class="container bg-main mt-5 p-4 rounded shadow">
    <h2 class="mb-4">Můj účet</h2>

    <div class="row mb-3">
      <div class="col-md-6">
        <label>Uživatelské jméno:</label>
        <p class="form-control-plaintext">{{ user.username }}</p>
      </div>
      <div class="col-md-6">
        <label>Email:</label>
        <p class="form-control-plaintext">{{ user.email }}</p>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label>Role:</label>
        <p class="form-control-plaintext">{{ roleName(user.roleId) }}</p>
      </div>
      <div class="col-md-6">
        <label>Datum registrace:</label>
        <p class="form-control-plaintext">{{ formatDate(user.registrationDate)}}</p>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label>Počet přidaných recenzí:</label>
        <p class="form-control-plaintext">{{ user.reviewCount ?? 'Načítám...' }}</p>
      </div>
      <div class="col-md-6">
        <label>Počet přidaných postav:</label>
        <p class="form-control-plaintext">{{ user.characterCount ?? 'Načítám...' }}</p>
      </div>
    </div>

    <hr />
    <ChangePasswordForm :user="user" />

  </div>
</template>

<script>
import { useUserStore } from "../stores/userStore";
import axios from "axios";
import ChangePasswordForm from "@/components/ChangePasswordForm.vue";

export default {
  components: {
    ChangePasswordForm
  },
  data() {
    return {
      password: {
        old: "",
        new: "",
      },
      user: {
        username: "",
        email: "",
        role: 0,
        characterCount: 0,
      },
    };
  },
  async mounted() {
    const store = useUserStore();
    try {
      const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/users/profile`, { withCredentials: true });
      this.user = { ...response.data };
      console.log(this.user)
      // Můžeš volat i /api/user-stats nebo něco, co ti dá třeba počet postav
    } catch (error) {
      console.error("Chyba při načítání dat o uživateli:", error);
    }
  },
  methods: {
    formatDate(dateString) {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) {
        return 'Neplatné datum';
      }

      return new Intl.DateTimeFormat('cs-CZ', {
        dateStyle: 'medium',
        timeStyle: 'short',
      }).format(date);
    },
    roleName(role) {
      switch (role) {
        case 1:
          return "Uživatel";
        case 2:
          return "Editor";
        case 3:
          return "Moderátor";
        case 4:
          return "Administrátor";
        default:
          return "Neznámá role";
      }
    },
  },
};
</script>

<style scoped>
.form-control-plaintext {
  background-color: #f8f9fa;
  padding: 0.375rem 0.75rem;
  border-radius: 0.25rem;
}
</style>
