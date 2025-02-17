<template>
  <div class="container">
    <h1>Email Verification</h1>
    <div v-if="successMessage" class="success">
      {{ successMessage }}
    </div>
    <div v-if="error" class="error">
      ERROR: {{ error }}
    </div>
    <form v-if="!successMessage && !error" @submit.prevent="verifyEmail">
      <button type="submit">Verify Email</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'VerificationPage',
  data() {
    return {
      email: '',
      code: '',  
      successMessage: '',
      error: null
    };
  },
  mounted() {
    const urlParams = new URLSearchParams(window.location.search);
    this.email = urlParams.get('email') || '';
    this.code = urlParams.get('code') || '';


    if (!this.email | this.code) {
      this.error = 'Invalid email or code';
    }
  },
  methods: {
    verifyEmail() {
axios.get(`http://localhost:8080/auth/verifyEmailAndCode?email=${this.email}&code=${this.code}`)
        .then(() => {
          this.successMessage = 'Email verified successfully!';
          this.$router.push({ name: 'Login' });
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

<style scoped>
.container {
  padding: 20px;
}
.success {
  color: green;
}
.error {
  color: red;
}
</style>
