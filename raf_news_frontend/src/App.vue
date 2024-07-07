<script setup>
import { RouterView } from 'vue-router'
</script>

<script>
import jwt_decode from 'jwt-decode'

export default {
  name: 'App',
  mounted() {
    const token = localStorage.getItem('token')

    if (token === null) {
      return
    }

    const decodedToken = jwt_decode(token)
    this.userRole = decodedToken.role
    this.userName = decodedToken.name
  },
  data() {
    return {
      userRole: '',
      userName: ''
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token')

      window.location.reload()
    }
  }
}
</script>

<template>
  <header>
    <div>
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark p-2 justify-content-between">

        <ul class="navbar-nav">
          <a class="navbar-brand" href="/">RAF News</a>
          <li class="nav-item active">
            <a class="nav-link" href="/">Home</a>
          </li>
          <li class="nav-item">
            <a v-if="userRole === ''" class="nav-link" href="/login">Login</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/popular">Popular</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/categories">Categories</a>
          </li>
          <li class="nav-item">
            <a v-if="userRole === 'Admin'" class="nav-link" href="/users">Users</a>
          </li>
        </ul>
        <div class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link disabled">{{ userName }}</a>
            </li>
            <li v-if="userRole !== ''" @click="logout()" class="logout-btn">
              <a class="nav-link">Logout</a>
            </li>
          </div>
      </nav>
    </div>
  </header>
  <div class="wrapper">
    <RouterView />
  </div>
</template>

<style>
#app {
  display: block;
}

.logout-btn:hover {
  cursor: pointer;
}
</style>
