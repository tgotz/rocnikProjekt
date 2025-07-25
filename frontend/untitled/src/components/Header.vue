<template>
  <header>
    <div class="container">
      <nav class="navbar navbar-dark navbar-expand-lg">
        <div class="container-fluid">
          <router-link
              class="navbar-brand"
              :to="{
                 path: '/',
                 query: {
                 showCartoon: 'true',
                 showIRL: 'true',
                  genders: ['Muž', 'Žena', 'Jiné'].join(','),
                },
                  }"
              @click="closeNavbar">
            <h1>
              <img class="navbar-logo" src="/images/logo.png" alt="logo" />
            </h1>
          </router-link>
          <button
              ref="navbarToggler"
              class="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNavDropdown"
              aria-controls="navbarNavDropdown"
              aria-expanded="false"
              aria-label="Toggle navigation"
              data-target="#navbar-collapse"
          >
            <span class="navbar-toggler-icon"></span>
          </button>
          <div ref="navbarCollapse" class="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
            <ul class="navbar-nav d-flex align-items-center">
              <li class="nav-item">
                <router-link
                    class="nav-link active"
                    :to="{
                 path: '/',
                 query: {
                 showCartoon: 'true',
                 showIRL: 'true',
                  genders: ['Muž', 'Žena', 'Jiné'].join(','),
                },
                  }"
                    @click="closeNavbar">Domů</router-link>
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
                    <router-link class="dropdown-item" :to="{ path: '/leaderboard', query: { sort: 1 } }" @click="closeNavbar">Nejoblíbenější</router-link>
                  </li>
                  <li>
                    <router-link class="dropdown-item" :to="{ path: '/leaderboard', query: { sort: 2 } }" @click="closeNavbar">Nejnenáviděnější</router-link>
                  </li>
                  <li>
                    <router-link class="dropdown-item" :to="{ path: '/leaderboard', query: { sort: 3 } }" @click="closeNavbar">Nejatraktivnější</router-link>
                  </li>
                </ul>
              </li>
              <li class="nav-item">
                <router-link class="nav-link text-white" to="/add-character" @click="closeNavbar">Přidat postavu</router-link>
              </li>
              <!-- administration dropdown - for logged in users only -->
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
                    <router-link class="dropdown-item" to="/approve-characters" @click="closeNavbar">Schvalování postav</router-link>
                  </li>
                  <li v-if="user?.role && user.role >= 3">
                    <router-link class="dropdown-item" to="/dashboard" @click="closeNavbar">Správa postav</router-link>
                  </li>
                  <li v-if="user?.role && user.role >= 4">
                    <router-link class="dropdown-item" to="/manage-users" @click="closeNavbar">Správa uživatelů</router-link>
                  </li>
                  <li v-if="user?.role && user.role >= 3">
                    <router-link class="dropdown-item" to="/reports-dashboard" @click="closeNavbar">Správa nahlášení</router-link>
                  </li>
                </ul>
              </li>
              <li class="nav-item dropdown" style="cursor: pointer;">
                <!-- desktop - icon -->
                <a
                    v-if="isLoggedIn"
                    class="nav-link d-none d-lg-block fs-2"
                    href="#"
                    role="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                  <i class="bi bi-person-circle logged-in fs-2"></i>
                </a>
                <a
                    v-else
                    class="nav-link text-white logged-out d-none d-lg-block fs-2"
                    @click="redirectToLogin"
                >
                  <i class="bi bi-person-circle"></i>
                </a>

                <!-- Mobile device - text -->
                <a
                    v-if="isLoggedIn"
                    class="nav-link text-white d-block d-lg-none"
                    href="#"
                    role="button"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                  Můj účet
                </a>
                <a
                    v-else
                    class="nav-link text-white d-block d-lg-none"
                    @click="redirectToLogin"
                >
                  Můj účet
                </a>

                <!-- DROPDOWN MENU (funguje pro obě varianty) -->
                <ul class="dropdown-menu dropdown-menu-end" v-if="isLoggedIn">
                  <li class="dropdown-item">
                    <strong>Uživatel:</strong> {{ user?.username }}
                  </li>
                  <li class="dropdown-item">
                    <strong>Role:</strong> {{ roleLabel }}
                  </li>
                  <li>
                    <router-link to="/my-account" class="dropdown-item" @click="closeNavbar">Můj účet</router-link>
                  </li>
                  <li>
                    <button class="dropdown-item" @click="logout">Odhlásit se</button>
                  </li>
                </ul>
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
    const userStore = useUserStore();
    const navbarCollapse = ref(null);
    const navbarToggler = ref(null);
    const user = computed(() => userStore.user);
    const isLoggedIn = computed(() => userStore.isLoggedIn);

    const checkLoginStatus = async () => {
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
        console.error("Chyba při ověřování uživatele:", error);
        userStore.clearUser();
      }
    };

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

    const logout = async () => {
      try {
        await axios.post(`${import.meta.env.VITE_API_URL}/api/auth/logout`, {}, {withCredentials: true});
        userStore.clearUser();
        closeNavbar();
      } catch (error) {
        console.error("Chyba při odhlašování:", error);
      }
    };

    const redirectToLogin = () => {
      window.location.href = "/login";
    };

    // Function to close the navbar collapse using template refs
    const closeNavbar = () => {
      if (navbarCollapse.value && navbarCollapse.value.classList.contains('show')) {
        navbarCollapse.value.classList.remove('show');
        if (navbarToggler.value) {
          navbarToggler.value.setAttribute('aria-expanded', 'false');
          navbarToggler.value.classList.add('collapsed');
        }
      }
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
      closeNavbar,
      navbarCollapse,
      navbarToggler,
    };
  },
};
</script>

<style>
.navbar-nav li {
  text-shadow: 0 0px 3px rgba(0, 0, 0, 0.8);
}

.bi-person-circle {
  text-shadow: none;
}

.dropdown-menu .dropdown-item {
  text-shadow: none;
}
</style>