<template>
  <div class="container bg-main pt-2">
    <h2 class="mt-5 mb-5 outline-heading">Správa uživatelů</h2>

    <!-- Vyhledávání uživatelů -->
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
        <td>{{ user.name }}</td>
        <td>{{ user.email }}</td>
        <td>
          <!-- Změna role je povolena pouze pro uživatele s rolemi 1-3 -->
          <select
              v-if="user.role >= 1 && user.role <= 3"
              class="form-select"
              v-model="user.role"
              @change="updateUserRole(user.id, user.role)"
          >
            <option value="1">Uživatel</option>
            <option value="2">Ověřený uživatel</option>
            <option value="3">Moderátor</option>
          </select>
          <span v-else>{{ getRoleLabel(user.role) }}</span>
        </td>
        <td>
          <button
              class="btn btn-danger btn-sm"
              @click="deleteUser(user.id)"
              :disabled="user.role === 4"
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
      users: [], // Seznam všech uživatelů
      filteredUsers: [], // Filtrovaný seznam podle vyhledávání
      searchQuery: "", // Hodnota pro vyhledávání
    };
  },
  methods: {
    // Načtení uživatelů
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
    // Filtrování uživatelů podle vyhledávání
    filterUsers() {
      const query = this.searchQuery.toLowerCase();
      this.filteredUsers = this.users.filter((user) =>
          user.name.toLowerCase().includes(query)
      );
    },
    // Vrací textový popis role
    getRoleLabel(role) {
      switch (role) {
        case 1:
          return "Uživatel";
        case 2:
          return "Ověřený uživatel";
        case 3:
          return "Moderátor";
        case 4:
          return "Administrátor";
        default:
          return "Neznámá role";
      }
    },
    // Aktualizace role uživatele
    async updateUserRole(userId, newRole) {
      try {
        if (newRole === 4) {
          alert("Roli Administrátora nelze měnit.");
          return;
        }

        await axios.post(
            "http://localhost:8080/api/update-user-role",
            {
              userId: userId,
              newRole: newRole,
            },
            {withCredentials: true}
        );
        alert("Role uživatele byla úspěšně aktualizována.");
      } catch (error) {
        console.error("Chyba při aktualizaci role uživatele:", error);
        alert("Při aktualizaci role došlo k chybě.");
      }
    },
    // Smazání uživatele
    async deleteUser(userId) {
      if (!confirm("Opravdu chcete tohoto uživatele smazat?")) return;

      try {
        axios.delete(`http://localhost:8080/api/delete-user?userId=${userId}`, {
          withCredentials: true,
        });
        this.users = this.users.filter((user) => user.id !== userId);
        this.filterUsers(); // Obnoví filtr
        alert("Uživatel byl úspěšně smazán.");
      } catch (error) {
        console.error("Chyba při mazání uživatele:", error);
        alert("Při mazání uživatele došlo k chybě.");
      }
    },
  },
  mounted() {
    this.fetchUsers(); // Načte uživatele při zobrazení stránky
  },
};
</script>
