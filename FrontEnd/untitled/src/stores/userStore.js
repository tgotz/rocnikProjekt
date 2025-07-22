import { defineStore } from "pinia";
import axios from "axios";

export const useUserStore = defineStore("user", {
    state: () => ({
        user: null,
    }),
    getters: {
        isLoggedIn: (state) => !!state.user, // checking if user is logged in
        userId: (state) => state.user?.userId || null, // getting id
    },
    actions: {
        setUser(userData) {
            this.user = userData;
        },
        clearUser() {
            this.user = null;
        },
        async fetchUser() {
            try {
                const response = await axios.get(`${import.meta.env.VITE_API_URL}/api/users/user-info`);
                this.user = response.data;
            } catch (error) {
                console.log("jsem v routeru")
                console.error("Chyba při načítání uživatele:", error);
                if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                    // Smazání tokenu z cookies
                    await axios.post(`${import.meta.env.VITE_API_URL}/api/auth/logout`, {}, { withCredentials: true });
                    this.clearUser();
                }

                this.user = null;
            }
            console.log("jsem ve fetchi")
            console.log(this.user)
        },
    },
});
