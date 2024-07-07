<template>
  <div class="user-edit-wrapper mt-5">
    <h1 class="text-center mb-5">Edit user</h1>
    <form class="w-25 mx-auto" @submit.prevent="editUser()">
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
        <label class="mb-3">Role</label>
        <select class="form-select" v-model="type">
          <option value="Admin">Admin</option>
          <option value="Creator">Content creator</option>
        </select>
      </div>
      <div class="form-group mb-5">
        <label class="mb-3">Status</label>
        <select class="form-select" v-model="status">
          <option value="Active">Active</option>
          <option value="Inactive">Inactive</option>
        </select>
      </div>
      <button class="btn btn-primary" type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'UserEditView',
  async mounted() {
    await this.fetchUser(this.$route.params['id'])
  },
  data() {
    return {
      user: null,
      firstName: '',
      lastName: '',
      email: '',
      type: '',
      status: '',
      passHash: ''
    }
  },
  methods: {
    async fetchUser(id) {
      const token = localStorage.getItem('token')
      if (token === null) {
        alert('User not logged in')
        return
      }

      const response = await fetch(`/backend/api/user/${id}`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + token
        }
      })

      const resData = await response.json()
      
      if (resData.email !== null) {
        this.user = resData
        this.email = resData.email;
        this.firstName = resData.firstName;
        this.lastName = resData.lastName;
        this.type = resData.type;
        this.status = resData.status ? 'Active' : 'Inactive';
        this.passHash = resData.passHash;
      }
    },
    async editUser() {
      const newUser = {
        id: this.$route.params.id,
        email: this.email,
        firstName: this.firstName,
        lastName: this.lastName,
        type: this.type,
        status: this.status === "Active",
        passHash: this.passHash
      }

      const token = localStorage.getItem('token')
      if (token === null) {
        alert('User not logged in')
        return
      }

      const response = await fetch('/backend/api/user/update', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + token
        },
        body: JSON.stringify(newUser)
      })

      if (response.status === 500) {
        alert('Server side error')
        return
      }

      if (response.status === 200) {
        alert('Success!')
      }

      const resData = await response.json()
    }
  }
}
</script>

<style>
.user-edit-wrapper {
  justify-content: center;
  align-items: center;
}
</style>
