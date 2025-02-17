import LoginPage from "@/components/LoginPage.vue";
import RegistrationPage from "@/components/RegistrationPage.vue";
import { createWebHistory, createRouter } from "vue-router";
import VerificationPage from "./components/VerificationPage.vue";
import UserDashboard from "./components/UserDashboard.vue";
import AdminPage from "./components/AdminPage.vue";

const routes = [

  {
    path: "/",
    redirect: "/register"
  },
  {
    path: "/login",
    name: "Login",
    component: LoginPage
  },
  {
    path: "/register",
    name: "Register",
    component: RegistrationPage
  },
  {
    path: "/verification",
    name: "Verification",
    component: VerificationPage
  },
  {
    path: "/userDashboard",
    name: "Dashboard",
    component: UserDashboard

  },
  {
    path: "/adminPage",
    name: "AdminPage",
    component: AdminPage
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});


export default router;