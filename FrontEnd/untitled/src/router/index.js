import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import CharacterPage from '../views/CharacterPage.vue';
import AddCharacterPage from '../views/AddCharacterPage.vue'; // Import přidání postavy
import LeaderboardPage from '../views/LeaderboardPage.vue';
import LoginPage from "@/views/LoginPage.vue";
import ApproveCharacter from "@/views/ApproveCharacter.vue";
import Dashboard from "@/views/Dashboard.vue";
import EditCharacter from "@/views/EditCharacter.vue";
import UsersDashboard from "@/views/UsersDashboard.vue";
import {useUserStore} from "@/stores/userStore.js"; // Import pro žebříčky

const routes = [
    {
        path: '/',
        name: 'Home',
        component: HomePage,
        beforeEnter: (to, from, next) => {
            const query = { ...to.query };

            // Výchozí hodnota pro stránku
            if (!query.page) query.page = '1';

            // Pokud chybí některé filtry, nastavíme výchozí hodnoty
            if (!query.search) query.search = '';
            if (!query.showCartoon) query.showCartoon = 'true';
            if (!query.showIRL) query.showIRL = 'true';
            if (!query.genders) query.genders = 'Muž,Žena,Jiné';
            if (!query.sortOrder) query.sortOrder = '';

            // Aktualizuj URL s výchozími hodnotami, pokud něco chybí
            if (JSON.stringify(query) !== JSON.stringify(to.query)) {
                next({ path: to.path, query });
            } else {
                next(); // Pokračuj na stránku, pokud vše v pořádku
            }
        },
    },
    {
        path: '/detail/:id',
        name: 'Character',
        component: CharacterPage,
        props: true, // Předá ID jako prop do komponenty
    },
    {
        path: '/add-character',
        name: 'AddCharacter',
        component: AddCharacterPage, // Nová stránka pro přidání postavy
    },
    {
        path: '/leaderboard',
        name: 'Leaderboard',
        component: LeaderboardPage, // Stránka pro žebříčky
        props: (route) => ({ sort: route.query.sort || '1' }), // Přidáme prop pro řazení na základě URL query parametru
    },
    {
        path: "/login",
        name: "Login",
        component: LoginPage,
    },
    {
        path: '/approve-characters',
        name: 'ApproveCharacters',
        component: ApproveCharacter,
        beforeEnter(to, from, next){
            const userStore = useUserStore();
            if(userStore.user?.role > 1){
                next();
            }else{
                next("/")
            }
        }
    },
    {
        path: "/dashboard",
        name: "Dashboard",
        component: Dashboard,
        beforeEnter(to, from, next){
            const userStore = useUserStore();
            if(userStore.user?.role > 2){
                next();
            }else{
                next("/")
            }
        }
    },
    {
        path: "/edit-character/:id",
        name: "EditCharacter",
        component: EditCharacter,
        props: true,
        beforeEnter(to, from, next){
            const userStore = useUserStore();
            if(userStore.user?.role > 2){
                next();
            }else{
                next("/")
            }
        }
    },
    {
        path: "/manage-users",
        name: "ManageUsers",
        component: UsersDashboard,
        beforeEnter(to, from, next){
            const userStore = useUserStore();
            if(userStore.user?.role > 3){
                next();
            }else{
                next("/")
            }
        }
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
