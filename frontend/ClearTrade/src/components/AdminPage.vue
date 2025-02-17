<template>
  <div class="admin-container">

    <h1 class="dashboard-title">Admin Dashboard</h1>
    <div v-if="error" class="error-response-admin">
      <h1>{{ error }}</h1>
    </div>

    <div v-else>
      <h2>Users</h2>
      <ul v-if="users.length" class="user-list">
        <li v-for="user in users" :key="user.userId" class="user-item">
          <p><strong>Username:</strong> {{ user.username }}</p>
          <p><strong>Role:</strong> {{user.role}}</p>
          <button @click="deleteUser(user.userId)" class="delete-btn">Delete</button>
    
        </li>
        <div v-if="deleteError" class="error-response-admin">
            <h1>{{ deleteError }}</h1>
        </div>    

       
      </ul>
      <p v-else>No users available</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AdminDashboard",

  data() {
    return {
      users: [],
      transactions: [],
      error: "", 
      deleteError: "",
    };
  },

  methods: {
    handleError(error) {
      if (error.response && error.response.status === 403) {
        this.error = "Access Denied. You do not have permission."; 
      } else if (error.response && error.response.data) {
        this.error = error.response.data;
      } else {
        this.error = error.message || "An unexpected error occurred";
      }
    },

    fetchUsers() {
      axios
        .get("http://localhost:8080/adminPanel/getAllUsers", {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        })
        .then((response) => {
          this.users = response.data;
        })
        .catch((error) => {
          this.handleError(error);
        });
    },

    deleteUser(userId) {
      axios
        .delete(`http://localhost:8080/adminPanel/${userId}`, {
          headers: { Authorization: `Bearer ${localStorage.getItem("token")}` },
        })
        .then((response) => {
          console.log(`✅ 200 OK: User ${userId} deleted successfully`, response)
          this.deleteError = '',
          this.fetchUsers();
        })
        .catch((error) => {
          if (error.response && error.response.status === 400 && error.response.data === "You can't delete an admin user") {
        this.deleteError = "You can't delete an admin user";
      } else {
        this.handleError(error);
      }
    });
    },

  },

  created() {
    this.fetchUsers();
  },
};
 </script>
  
  <style src="@/assets/AdminPage.css"></style>
  