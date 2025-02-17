<template>
  <div class="login-page">
    <h1 class="site-name">Welcome to ClearTrade</h1>
    <div class="container">
      
      <!-- Форма входа -->
      <form v-if="!showResetForm && !showForgotPassword && !isVerifying && !showVerificationForm" @submit.prevent="login">     
         <h1>Sign In</h1>
        <div>
          <label for="username">Username:</label>
          <input type="text" v-model="username" id="username" required />
          <p v-if="errors.username" class="error">{{ errors.username }}</p>
        </div>
        <div class="password-container">
          <a href="#" class="forgot-password" @click.prevent="toggleForgotPassword">
            Forgot your password?
          </a>
          <label for="password">Password:</label>
          <input type="password" v-model="password" id="password" required />
          <p v-if="errors.password" class="error">{{ errors.password }}</p>
        </div>
        <button type="submit">Enter</button>
        <p v-if="error" class="error">{{ error }}</p>
      </form>
      <button @click="$router.push('/register')">Sign Up</button>

      <form v-if="showForgotPassword" class="forgot-password-form" @submit.prevent="sendResetLink">
        <h2>Forgot Password</h2>
        <input type="email" v-model="emailInput" placeholder="Enter your email" required />
        <p v-if="modalError" class="error">{{ modalError }}</p>
        <button type="submit">Send Reset Link</button>
        <button type="button" class="cancel" @click="toggleForgotPassword">Cancel</button>
      </form>

      <form v-if="showVerificationForm" class="verification-form" @submit.prevent="sendVerificationCode">
        <h2>Email Verification</h2>
        <input type="email" v-model="verificationEmail" placeholder="Enter your email" required />
        <p v-if="verificationError" class="error">{{ verificationError }}</p>
        <button type="submit">Send Verification Code</button>
        <button type="button" class="cancel" @click="showVerificationForm = false">Cancel</button>
        <p v-if="error" class="error">{{ error }}</p>

      </form>
      
      <form v-if="showResetForm" class="reset-form" @submit.prevent="resetPassword">
        <h2>Set New Password</h2>
        <input type="password" v-model="newPassword" placeholder="New Password" required />
        <input type="password" v-model="confirmNewPassword" placeholder="Confirm New Password" required />
        <p v-if="resetError" class="error">{{ resetError }}</p>
        <p v-if="resetSuccess" class="success">{{ resetSuccess }}</p>
        <button type="submit">Reset Password</button>
      </form>
      
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginPage',
  data() {
    return {
      username: '',
      password: '',
      verificationEmail: '',
      newPassword: '',
      confirmNewPassword: '',
      error: null,
      resetError: null,
      resetSuccess: null,
      modalError: null,
      verificationError: null,
      errors: {},
      successMessage: null,
      showForgotPassword: false,
      showResetForm: false,
      showVerificationForm: false,
      isVerifying: false,
    };
  },
  mounted() {
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    const code = urlParams.get('code');

    console.log("Email from URL:", email);
    console.log("Code from URL:", code);

    if (email && code) {
        this.emailInput = email; 
        this.isVerifying = true;
        this.verifyEmail(email, code);
    }
},  methods: {
    validate() {
      this.errors = {};
      this.error = null;
      if (!this.username.trim()) {
        this.errors.username = 'Username is mandatory';
      } else if (/\s{2,}/.test(this.username)) {
        this.errors.username = 'Username must not contain more than one consecutive space';
      } else if (this.username.length < 2) {
        this.errors.username = 'Username must be at least 2 characters long';
      } else if (this.username.length > 20) {
        this.errors.username = 'Username must not exceed 20 characters';
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

    login() {
      this.error = null;
      this.errors = {};
      axios.post('http://localhost:8080/auth/login', {
        username: this.username,
        password: this.password
      }).then(response => {
        const token = response.data;
        localStorage.setItem('token', response.data);

        const payload = JSON.parse(atob(token.split('.')[1]));
        const userRole = payload.role;

        if (userRole === 'ADMIN') {
      this.$router.push({ name: 'AdminPage' });
    } else {
      setTimeout(() => {
          this.$router.push({ name: 'Dashboard' });
        }, 2000);    }
        this.successMessage = 'Login successful! Redirecting to dashboard...';
      }).catch(error => {
        this.handleError(error);
        if(error.response.data == "Email is not verified") {
          this.showVerificationForm = true;
        }
      });

    },
    sendResetLink() {
      this.modalError = null;
      axios.post("http://localhost:8080/auth/sendLinkToResetPassword", { email: this.emailInput })
        .then(() => {
          alert("Password reset link sent! Check your email.");
          this.showForgotPassword = false;
        })
        .catch(error => {
          this.modalError = this.handleError(error);
        });
    },

    sendVerificationCode() {
      this.error = null;
      axios.post("http://localhost:8080/auth/sendVerificationCodeOnEmail", { email: this.verificationEmail })
        .then(() => {
          alert("Verification code sent! Check your email.");
          this.showVerificationForm = false;
        })
        .catch(error => {
          this.verificationError = this.handleError(error);
        });
    },
    handleError(error) {
      if (error.response && error.response.data) {
        return error.response.data;
      } else {
        return error.message || 'An unexpected error occurred';
      }
    },
    verifyEmail(email, code) {
        axios.get(`http://localhost:8080/auth/verifyPasswordAndCode?email=${email}&code=${code}`)
            .then(() => {
                console.log("Verification successful, showing reset form");
                this.showResetForm = true;
            })
            .catch((error) => {
                console.error("Verification failed:", error);
                this.error = this.handleError(error);
            })
            .finally(() => {
                this.isVerifying = false;
            });
    },
    resetPassword() {
      this.error = null;
      if (this.newPassword !== this.confirmNewPassword) {
        this.resetError = 'Passwords do not match';
        return;
      }
      axios.post('http://localhost:8080/auth/resetPassword', {
        email: this.emailInput,
        newPassword: this.newPassword,
        newPasswordConfirm: this.confirmNewPassword
      }).then(() => {
        this.resetSuccess = 'Password reset successfully!';
        setTimeout(() => this.$router.push({ name: 'Login' }), 2000);
      }).catch(error => {
        this.resetError = this.handleError(error);
      });
    },
    toggleForgotPassword() {
      this.error = null;
  this.modalError = null;
  this.showForgotPassword = !this.showForgotPassword;
  this.showResetForm = false;
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

<style src="@/assets/LoginPage.css"></style>
