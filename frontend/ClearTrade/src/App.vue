<template>
  <div id="app">
    <router-view/> 
     </div>
</template>

<script>
import RegistrationPage from './components/RegistrationPage.vue';
import LoginPage from './components/LoginPage.vue';
import VerificationPage from './components/VerificationPage.vue';
import UserDashboard from './components/UserDashboard.vue';
import AdminPage from './components/AdminPage.vue';
export default {
  name: 'App',
  components: {
    RegistrationPage,
    LoginPage,
    VerificationPage,
    UserDashboard,
    AdminPage
  },
};
</script>

<style>
#app {
  text-align: center;
  margin: 20px;
}


</style>
