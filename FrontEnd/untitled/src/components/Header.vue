<template>
  <header>
    <div class="container">
      <nav class="navbar navbar-dark navbar-expand-lg">
        <div class="container-fluid">
          <!-- Logo -->
          <router-link class="navbar-brand" to="/">
            <h1>
              <img class="navbar-logo" src="/images/logo.png" alt="logo" />
            </h1>
          </router-link>
          <!-- Mobilní toggle -->
          <button
              class="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNavDropdown"
              aria-controls="navbarNavDropdown"
              aria-expanded="false"
              aria-label="Toggle navigation"
          >
            <span class="navbar-toggler-icon"></span>
          </button>
          <!-- Navigace -->
          <div class="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
            <ul class="navbar-nav d-flex align-items-center">
              <!-- Odkazy -->
              <li class="nav-item">
                <router-link class="nav-link active" aria-current="page" to="/">Domů</router-link>
              </li>
              <li class="nav-item dropdown">
                <a
                    class="nav-link dropdown-toggle text-white"
                    href="#"
                    role="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                  Žebříčky
                </a>
                <ul class="dropdown-menu">
                  <li>
                    <router-link class="dropdown-item" :to="{ path: '/leaderboard', query: { sort: 1 } }">Nejoblíbenější</router-link>
                  </li>
                  <li>
                    <router-link class="dropdown-item" :to="{ path: '/leaderboard', query: { sort: 2 } }">Nejnenáviděnější</router-link>
                  </li>
                  <li>
                    <router-link class="dropdown-item" :to="{ path: '/leaderboard', query: { sort: 3 } }">Nejatraktivnější</router-link>
                  </li>
                </ul>
              </li>
              <li class="nav-item">
                <router-link class="nav-link text-white" to="/add-character">Přidat postavu</router-link>
              </li>
              <!-- Administrace Dropdown -->
              <li
                  class="nav-item dropdown"
                  style="cursor: pointer;"
                  v-if="user?.role && user.role >= 2"
              >
                <a
                    class="nav-link dropdown-toggle text-white"
                    href="#"
                    role="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                  Administrace
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                  <li v-if="user?.role && user.role >= 2">
                    <router-link class="dropdown-item" to="/approve-characters">Schvalování postav</router-link>
                  </li>
                  <li v-if="user?.role && user.role >= 3">
                    <router-link class="dropdown-item" to="/dashboard">Správa postav</router-link>
                  </li>
                  <li v-if="user?.role && user.role >= 4">
                    <router-link class="dropdown-item" to="/manage-users">Správa uživatelů</router-link>
                  </li>
                </ul>
              </li>
              <!-- Ikonka uživatele -->
              <li class="nav-item dropdown fs-2" style="cursor: pointer;">
                <a
                    v-if="isLoggedIn"
                    class="nav-link"
                    href="#"
                    role="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                  <i class="bi bi-person-circle logged-in"></i>
                </a>
                <ul class="dropdown-menu dropdown-menu-end" v-if="isLoggedIn">
                  <li class="dropdown-item">
                    <strong>Uživatel:</strong> {{ user?.username }}
                  </li>
                  <li class="dropdown-item">
                    <strong>Role:</strong> {{ roleLabel}}
                  </li>
                  <li>
                    <button class="dropdown-item" @click="logout">Odhlásit se</button>
                  </li>
                </ul>
                <a
                    v-else
                    class="nav-link text-white logged-out"
                    @click="redirectToLogin"
                >
                  <i class="bi bi-person-circle"></i>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script>
import { useUserStore } from "../stores/userStore";
import axios from "axios";
import { ref, computed, onMounted } from "vue";

export default {
  setup() {
    const userStore = useUserStore(); // Přístup k Pinia store
    const user = computed(() => userStore.user); // Dynamická vazba na uživatele
    const isLoggedIn = computed(() => userStore.isLoggedIn); // Kontrola přihlášení

    // Kontrola stavu přihlášení
    const checkLoginStatus = async () => {
      try {
        const response = await axios.get("http://localhost:8080/user-info", {
          withCredentials: true, // Odesíláme HttpOnly cookies s tokenem
        });

        // Pokud je uživatel autentizován
        if (response.data.username) {
          userStore.setUser({
            username: response.data.username, // Nastavení username
            role: parseInt(response.data.role), // Nastavení role
            userId: parseInt(response.data.userId), // Nastavení userId
          });
        } else {
          userStore.clearUser(); // Pokud není přihlášen
        }
      } catch (error) {
        console.error("Chyba při ověřování uživatele:", error);
        userStore.clearUser();
      }
    };

    // Převod role na textový popis
    const roleLabel = computed(() => {
      if (!user.value?.role) return "Načítání role...";

      switch (user.value.role) {
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
    });

    // Odhlášení uživatele
    const logout = async () => {
      try {
        await axios.post(
            "http://localhost:8080/api/logout",
            {},
            { withCredentials: true }
        );
        userStore.clearUser();
      } catch (error) {
        console.error("Chyba při odhlašování:", error);
      }
    };

    // Přesměrování na přihlášení
    const redirectToLogin = () => {
      window.location.href = "/login";
    };

    onMounted(() => {
      checkLoginStatus();
    });

    return {
      user,
      isLoggedIn,
      checkLoginStatus,
      logout,
      redirectToLogin,
      roleLabel,
    };
  },
};
</script>
