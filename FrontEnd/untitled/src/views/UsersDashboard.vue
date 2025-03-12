<template>
  <div class="container bg-main pt-2">
    <h2 class="mt-5 mb-5 outline-heading">Správa uživatelů</h2>

    <div class="form-group mb-4">
      <input
          v-model="searchQuery"
          @input="filterUsers"
          type="text"
          class="form-control"
          placeholder="Vyhledat uživatele podle jména"
      />
    </div>

    <table class="table table-striped">
      <thead>
      <tr>
        <th>#</th>
        <th>Jméno</th>
        <th>Email</th>
        <th>Role</th>
        <th>Akce</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user, index) in filteredUsers" :key="user.id">
        <td>{{ index + 1 }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>

          <select
              v-if="user.role.id >= 1 && user.role.id <= 3"
              class="form-select"
              v-model="user.role.id"
              @change="updateUserRole(user.id, user.role.id)"
          >
            <option value="1">Uživatel</option>
            <option value="2">Ověřený uživatel</option>
            <option value="3">Moderátor</option>
          </select>
          <span v-else>{{ (user.role.roleName) }}</span>
        </td>
        <td>
          <button
              class="btn btn-danger btn-sm"
              @click="deleteUser(user.id)"
              :disabled="user.role.id === 4"
          >
            Smazat
          </button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [],
      filteredUsers: [],
      searchQuery: "",
    };
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get("http://localhost:8080/api/users", {
          withCredentials: true,
        });
        this.users = response.data;
        this.filteredUsers = this.users;
      } catch (error) {
        console.error("Chyba při načítání uživatelů:", error);
      }
    },
    filterUsers() {
      const query = this.searchQuery.toLowerCase();
      this.filteredUsers = this.users.filter((user) =>
          user.username.toLowerCase().includes(query) ||
          (user.email && user.email.toLowerCase().includes(query))
      );
    },

    async updateUserRole(userId, newRole) {
      try {
        if (newRole === 4) {
          alert("Roli Administrátora nelze měnit.");
          return;
        }

        await axios.put(
            `http://localhost:8080/api/users/${userId}/role`,
            null, // žádné body (data), jen query param
            {
              params: {
                roleId: newRole
              },
              withCredentials: true,
            }
        );

        alert("Role uživatele byla úspěšně aktualizována.");
      } catch (error) {
        console.error("Chyba při aktualizaci role uživatele:", error);
        alert("Při aktualizaci role došlo k chybě.");
      }
    },
    async deleteUser(userId) {
      if (!confirm("Opravdu chcete tohoto uživatele smazat?")) return;

      try {
        await axios.delete(`http://localhost:8080/api/users/${userId}`, {
          withCredentials: true,
        });

        // Po úspěšném smazání aktualizuj seznam uživatelů
        this.users = this.users.filter((user) => user.id !== userId);
        this.filterUsers(); // Obnoví filtr, pokud používáš nějaké vyhledávání
        alert("Uživatel byl úspěšně smazán.");
      } catch (error) {
        console.error("Chyba při mazání uživatele:", error);
        alert("Při mazání uživatele došlo k chybě.");
      }
    }
  },
  mounted() {
    this.fetchUsers();
  },
};
</script>
