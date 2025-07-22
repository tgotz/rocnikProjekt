import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import CharacterPage from '../views/CharacterPage.vue';
import AddCharacterPage from '../views/AddCharacterPage.vue';
import LeaderboardPage from '../views/LeaderboardPage.vue';
import LoginPage from "@/views/LoginPage.vue";
import ApproveCharacter from "@/views/ApproveCharacter.vue";
import Dashboard from "@/views/Dashboard.vue";
import EditCharacter from "@/views/EditCharacter.vue";
import UsersDashboard from "@/views/UsersDashboard.vue";
import { useUserStore } from "@/stores/userStore.js";
import RegisterPage from "@/views/RegisterPage.vue";
import MyAccount from "@/views/MyAccount.vue";
import ReportsDashboard from "@/views/ReportsDashboard.vue";

//function to check if user has permition to access certain site
const requireRole = (minRole) => (to, from, next) => {
    const userStore = useUserStore();
    if (userStore.isLoggedIn === false || userStore.user.role < minRole) {
        console.log("adios")
        next('/');
    } else {
        console.log("seš ok")
        next();
    }
};

const createAppRouter = async (pinia) => {
    const userStore = useUserStore(pinia); // Explicitně používáme Pinia instanci
    try {
        await userStore.fetchUser();
    } catch (error) {
        console.error("Chyba při načítání uživatele:", error);
    }

    const routes = [
        {
            path: '/',
            name: 'Home',
            component: HomePage,
            beforeEnter: (to, from, next) => {
                const query = { ...to.query };

                if (!query.page) query.page = '1';
                if (!query.search) query.search = '';
                if (!query.showCartoon) query.showCartoon = 'true';
                if (!query.showIRL) query.showIRL = 'true';
                if (!query.genders) query.genders = 'Muž,Žena,Jiné';
                if (!query.sortOrder) query.sortOrder = '';

                if (JSON.stringify(query) !== JSON.stringify(to.query)) {
                    next({ path: to.path, query });
                } else {
                    next();
                }
            },
        },
        {
            path: '/detail/:id',
            name: 'Character',
            component: CharacterPage,
            props: true,
        },
        {
            path: '/add-character',
            name: 'AddCharacter',
            component: AddCharacterPage,
        },
        {
            path: '/leaderboard',
            name: 'Leaderboard',
            component: LeaderboardPage,
            props: (route) => ({ sort: route.query.sort || '1' }),
        },
        {
            path: '/login',
            name: 'Login',
            component: LoginPage,
        },
        {
            path: '/approve-characters',
            name: 'ApproveCharacters',
            component: ApproveCharacter,
            beforeEnter: requireRole(2),
        },
        {
            path: '/dashboard',
            name: 'Dashboard',
            component: Dashboard,
            beforeEnter: requireRole(3),
        },
        {
            path: '/edit-character/:id',
            name: 'EditCharacter',
            component: EditCharacter,
            props: true,
            beforeEnter: requireRole(3),
        },
        {
            path: '/manage-users',
            name: 'ManageUsers',
            component: UsersDashboard,
            beforeEnter: requireRole(4),
        },
        {
            path: '/register',
            name: 'Register',
            component: RegisterPage
        },
        {
            path:'/my-account',
            name: 'MyAccount',
            component: MyAccount,
            beforeEnter: requireRole(1),
        },
        {
            path:'/reports-dashboard',
            name: 'ReportsDashboard',
            component: ReportsDashboard,
            beforeEnter: requireRole(3),
        }

];

    return createRouter({
        history: createWebHistory(),
        routes,
    });
};

export default createAppRouter;
