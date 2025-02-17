<template>
  <h1 class="site-name">Welcome to ClearTrade</h1>
  <div class="container">
    <h1>Registration</h1>
    <form @submit.prevent="register">
      <div>
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" required />
        <p v-if="errors.username" class="error">{{ errors.username }}</p>
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" v-model="email" id="email" required />
        <p v-if="errors.email" class="error">{{ errors.email }}</p>
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
        <p v-if="errors.password" class="error">{{ errors.password }}</p>
      </div>
      <button type="submit">Register</button>
      <div v-if="showVerification">
      </div>
    </form>
    <button @click="$router.push('/login')">Sign In</button>
    <div v-if="successMessage" class="success">
      {{ successMessage }}
    </div>
    <div v-if="error" class="error">
      ERROR: {{ error }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'RegistrationPage',
  data() {
    return {
      username: '',
      email: '',
      password: '',
      verificationCode: '',
      showVerification: false,
      error: null,
      successMessage: '',
      errors: {}
    };
  },
  methods: {
    resetSuccessMessage() {
      this.successMessage = '';
    },

    resetError() {
      this.error = null;
    },

    validate() {
      this.errors = {};

      if (!this.username.trim()) {
        this.errors.username = 'Username is mandatory';
      } else if (/\s{2,}/.test(this.username)) {
        this.errors.username = 'Username must not contain more than one consecutive space';
      } else if (this.username.length < 2) {
        this.errors.username = 'Username must be at least 2 characters long';
      } else if (this.username.length > 20) {
        this.errors.username = 'Username must not exceed 20 characters';
      }

      if (!this.email.trim()) {
        this.errors.email = 'Email is mandatory';
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email)) {
        this.errors.email = 'Valid email is mandatory';
      }

      if (!this.password.trim()) {
        this.errors.password = 'Password is mandatory';
      } else if (this.password.length < 8) {
        this.errors.password = 'Password must be at least 8 characters long';
      } else if (this.password.length > 20) {
        this.errors.password = 'Password must not exceed 20 characters';
      }

      return Object.keys(this.errors).length === 0;
    },

    register() {
      if (!this.validate()) {
        return;
      }

      const registerCommand = {
        username: this.username,
        email: this.email,
        password: this.password
      };

      axios.post('http://localhost:8080/auth/registration', registerCommand)
        .then(() => {
          this.successMessage = 'Registration successful! Please check your email for the verification link.';
          this.showVerification = true;
        })
        .catch(error => {
          this.handleError(error);
        });
    },
    handleError(error) {
      if (error.response && error.response.data) {
        this.error = error.response.data;
      } else {
        this.error = error.message || 'An unexpected error occurred';
      }
    }
  }
};
</script>

<style src="@/assets/RegisterPage.css"></style>
