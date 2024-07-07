<template>
  <div class="container login-wrapper">
    <h1 class="text-center mt-5 mb-5">Login</h1>

    <form class="w-50 mx-auto" @submit.prevent="login()">
      <div class="form-group mb-3">
        <label>Email address</label>
        <input
          type="email"
          class="form-control"
          placeholder="Enter email"
          v-model="email"
        />
      </div>
      <div class="form-group mb-3">
        <label>Password</label>
        <input
          type="password"
          class="form-control"
          placeholder="Password"
          v-model="password"
        />
      </div>

      <button type="submit" class="btn btn-primary">Submit</button>
    </form>
  </div>
</template>

<script>
import jwt_decode from "jwt-decode"

export default {
  name: 'Login',
  data() {
    return  {
        email: "",
        password: ""
    }
  },
  methods: {
    async login() {
        const response = await fetch("/backend/api/user/login", {
            method: "POST",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                email: this.email,
                password: this.password
            })
        });

        const resData = await response.json();

        if(resData.message){
          alert(resData.message);
          return;
        }

        localStorage.setItem("token", resData.jwt)

        window.location = "/home"
    }
  }
}
</script>

<style>
.login-wrapper {
  justify-content: center;
  align-items: center;
}
</style>
