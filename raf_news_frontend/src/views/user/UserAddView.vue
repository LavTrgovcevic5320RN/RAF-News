<template>
  <div class="user-new-wrapper mt-5">
    <h1 class="text-center mb-5">New user</h1>
    <form class="w-25 mx-auto" @submit.prevent="addUser()">
      <div class="form-group mb-3">
        <label class="mb-3">First name</label>
        <input type="text" class="form-control" v-model="firstName" required />
      </div>
      <div class="form-group mb-5">
        <label class="mb-3">Last name</label>
        <input type="text" class="form-control" v-model="lastName" required />
      </div>
      <div class="form-group mb-5">
        <label class="mb-3">E-mail</label>
        <input type="email" class="form-control" v-model="email" required />
      </div>
      <div class="form-group mb-5">
        <label class="mb-3">Last name</label>
        <select class="form-select" v-model="type">
          <option value="Admin">Admin</option>
          <option value="Creator">Content creator</option>
        </select>
      </div>
      <div class="form-group mb-5">
        <label class="mb-3">Password</label>
        <input type="password" class="form-control" v-model="password" required />
      </div>
      <button class="btn btn-primary" type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'UserAddView',
  data() {
    return {
      firstName: '',
      lastName: '',
      email: '',
      type: 'Admin',
      password: ''
    }
  },
  methods: {
    async addUser() {
      const token = localStorage.getItem('token')
      if (token === null) {
        alert('User not logged in')
        return
      }

      const newUser = {
        email: this.email,
        firstName: this.firstName,
        lastName: this.lastName,
        type: this.type,
        status: true,
        passHash: this.password
      }

      console.log(newUser)

      const res = await fetch('/backend/api/user/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + token
        },
        body: JSON.stringify(newUser)
      })

      if (res.status === 500) {
        alert('User with this email already exists!')
        return
      }

      const resJson = await res.json()

      if (resJson['email']) {
        alert('Success!')
      }
    }
  }
}
</script>

<style>
.user-add-wrapper {
  justify-content: center;
  align-items: center;
}
</style>
