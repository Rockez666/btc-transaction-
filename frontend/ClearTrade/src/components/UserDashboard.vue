<template>
  <div class="dashboard-container">
    <div v-if="showGreeting" class="greeting">
      <h1>Hello, {{ user?.username }}!</h1>
      <p>Welcome to the dashboard. Redirecting...</p>
    </div>

    <div v-else>
      <h1 class="dashboard-title">Welcome to the Dashboard</h1>
      <div v-if="error" class="error-response ">
        {{ error }}</div>
      <div v-if="user" class="user-details">
        <h2>Profile</h2>
        <p><strong>Username:</strong> <span class="username">{{ user.username }}</span></p>
        <button @click="toggleTransactions" class="toggle-btn">Toggle Transactions</button>
        <div v-if="showTransactions">
          <button @click="toggleCreateTransaction" class="transaction-create-button">Create Transaction</button>

          <div v-if="showCreateTransaction" class="create-transaction-form">
            <h3>Create a Transaction</h3>
            <form @submit.prevent="submitTransaction">
              <div>
                <label for="transactionType">Transaction Type:</label>
                <select v-model="transactionForm.transactionType" id="transactionType">
                  <option value="BUY">Buy</option>
                  <option value="SELL">Sell</option>
                </select>
                <p v-if="fieldError.transactionType" class="field-error">{{ fieldError.transactionType }}</p>
              </div>
       
              <div>
                <label for="cryptocurrency">Cryptocurrency:</label>
                <input type="text" v-model="transactionForm.cryptocurrency" id="cryptocurrency" />
                <p v-if="fieldError.cryptocurrency" class="field-error">{{ fieldError.cryptocurrency }}</p>
              </div>

              <div>
                <label for="price">Price (USDT):</label>
                <input type="number" v-model="transactionForm.price" id="price" step="0.01" />
                <p v-if="fieldError.price" class="field-error">{{ fieldError.price }}</p>
              </div>

              <div>
                <label for="quantity">Quantity:</label>
                <input type="number" v-model="transactionForm.quantity" id="quantity" step="0.01" />
                <p v-if="fieldError.quantity" class="field-error">{{ fieldError.quantity }}</p>
              </div>

              <p v-if="fieldError.general" class="field-error">{{ fieldError.general }}</p>
              <div v-if="error" class="error-response ">
      {{ error }} 
          </div>
              <button type="submit" class="submit-btn">Submit</button>
            </form>

            <div v-if="responseError" class="error-response">
              {{ responseError }}
            </div>
          </div>
          <h3>Transactions:</h3>
          <ul v-if="user.transactions && user.transactions.length > 0" class="transactions-list">
            <li v-for="transaction in user.transactions" :key="transaction.id" class="transaction-item">
              <p>
                <strong>Type:</strong> {{ transaction.transactionType }}
                <strong>Date:</strong> {{ formatDate(transaction.transactionDate) }}
                <strong>Assets:</strong> {{ transaction.cryptocurrency }}
                <strong>Quantity:</strong> {{ formatNumber(transaction.quantityInTransaction) }}
                <strong>Equivalent USD:</strong> {{ formatNumber(transaction.equivalentInUSD) }}
                <strong>Price:</strong> {{ formatNumber(transaction.price) }} USDT
              </p>
              <button @click="deleteTransaction(transaction.transactionId)" class="delete-btn">Delete</button>

            </li>
          </ul>
          <p v-else>No transactions available</p>
        </div>

        <h3>Token Statistics:</h3>
        <ul v-if="user.tokens && user.tokens.length > 0" class="tokens-list">
          <li v-for="token in user.tokens" :key="token.cryptocurrency" class="token-item">
            <p><strong>Cryptocurrency:</strong> {{ token.cryptocurrency }}</p>
            <p><strong>Total Tokens:</strong> {{ formatNumber(token.totalTokens) }}</p>
            <p><strong>Average Purchase Price:</strong> {{ formatNumber(token.averagePurchasePrice) }} USD</p>
            <p><strong>Average Sell Price:</strong> {{ formatNumber(token.averageSellPrice) }} USD</p>
            <p><strong>Equivalent USD:</strong> {{ formatNumber(token.equivalentUsd) }} USD</p>
          </li>
        </ul>
        <p v-else>No token statistics available</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "UserDashboard",

  data() {
    return {
      user: null,
      responseError: '',
      fieldError: {}, 
      error: '',
      showGreeting: true,
      showTransactions: false,
      showCreateTransaction: false,
      transactionForm: {
        transactionType: "BUY",
        cryptocurrency: "",
        price: 0,
        quantity: 0,
      },
    };
  },

  methods: {
    validateTransactionForm() {
      const errors = {};

      if (!this.transactionForm.transactionType) {
        errors.transactionType = "Transaction type is mandatory.";
      }

      if (!this.transactionForm.cryptocurrency) {
        errors.cryptocurrency = "Cryptocurrency is mandatory.";
      }

      if (this.transactionForm.price <= 0 || isNaN(this.transactionForm.price)) {
        errors.price = "Price should be positive.";
      }

      if (this.transactionForm.quantity <= 0 || isNaN(this.transactionForm.quantity)) {
        errors.quantity = "Quantity should be positive.";
      }

      return errors;
    },

    formatNumber(number) {
      return new Intl.NumberFormat("en-US").format(number);
    },

    formatDate(date) {
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(date).toLocaleDateString("en-US", options);
    },

    fetchUserData() {
      axios
        .get("http://localhost:8080/users/currentUserDto", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        })
        .then((response) => {
          this.user = response.data;
        })
        .catch((error) => {
          this.handleError(error);
        });
    },

    toggleTransactions() {
      this.showTransactions = !this.showTransactions;
    },

    toggleCreateTransaction() {
      this.showCreateTransaction = !this.showCreateTransaction;
    },

    resetForm() {
      this.transactionForm = {
        transactionType: "BUY",
        cryptocurrency: "",
        price: 0,
        quantity: 0,
      };
    },

    submitTransaction() {
      const errors = this.validateTransactionForm();

      this.clearErrors();

      if (Object.keys(errors).length > 0) {
        this.fieldError = errors;

        Object.keys(errors).forEach((key) => {
          this.setErrorTimeout(key);
        });

        return;
      }

      axios
        .post(
          "http://localhost:8080/transactions/createTransaction", 
          this.transactionForm,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        )
        .then((response) => {
          this.successMessage = response.data;
          this.fetchUserData();
          this.showCreateTransaction = false;
          this.resetForm();
        })
        .catch((error) => {
          this.handleError(error);
        });
    },


deleteTransaction(transactionId) {
  axios
    .delete(`http://localhost:8080/transactions/deleteTransaction/${transactionId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
    })
    .then(() => {
      this.fetchUserData();
    })
    .catch((error) => {
      this.handleError(error);
    });
},

    handleError(error) {
      if (error.response && error.response.status === 403) {
      this.error = 'Access Denied. You do not have permission..';
    } else if (error.response && error.response.data) {
      this.error = error.response.data;
    } else {
      this.error = error.message || 'An unexpected error occurred';
    }

      setTimeout(() => {
    this.responseError = '';
  }, 2000);
    },

    clearErrors() {
      this.error= null;
      this.fieldError = {};
    },

    setErrorTimeout(field) {
      setTimeout(() => {
        this.$set(this.fieldError, field, null);
      }, 3000);
    },
  },

  created() {
    this.fetchUserData();

    setTimeout(() => {
      this.showGreeting = false;
    }, 3000);
  },
};
</script>

<style src="@/assets/UserDashboard.css"></style>
