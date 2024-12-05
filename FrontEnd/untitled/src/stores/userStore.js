import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
    state: () => ({
        user: null, // Uživatelská data
    }),
    getters: {
        isLoggedIn: (state) => !!state.user, // Kontrola přihlášení
        userId: (state) => state.user?.userId || null, // Získání userId
    },
    actions: {
        setUser(userData) {
            this.user = userData; // Nastaví data uživatele
        },
        clearUser() {
            this.user = null; // Vymaže data uživatele
        },
    },
});
