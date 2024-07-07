<template>
    <div class="container user-wrapper mt-5">
      <h1 class="text-center">Users</h1>
      <div class="d-flex mx-auto mt-5">
        <button class="btn btn-primary mx-auto" @click="navigateToNewUser()">New</button>
      </div>
      <div class="mx-auto">
        <table class="table table-dark table-stripped mt-5">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">First name</th>
              <th scope="col">Last name</th>
              <th scope="col">Email</th>
              <th scope="col">Type</th>
              <th scope="col">Edit</th>
              <th scope="col">Status</th>
            </tr>
          </thead>
          <tbody>
              <tr v-for="(user, idx) in users" :key="idx">
                  <th>{{ user.id }}</th>
                  <th>{{ user.firstName }}</th>
                  <th>{{ user.lastName }}</th>
                  <th>{{ user.email }}</th>
                  <th>{{ user.type }}</th>
                  <th>
                      <button class="btn btn-primary" @click="navigateToEditUser(user.id)">Edit</button>
                  </th>
                  <th>
                      <button class="btn btn-danger" v-if="user.type !== 'Admin'" @click="changeStatus(user.id, !user.status)">{{ user.status}}</button>
                  </th>
              </tr>
          </tbody>
        </table>
        
        <div class="pages-select justify-content-center">
          <div v-for="(pageNum) in pages" :key="pageNum" @click="changePage(pageNum)" class="page-num">
              {{ pageNum }}
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'UserList',
    async mounted() {
      await this.fetchUserPage(1);
    },
    data() {
      return  {
          users: [],
          pages: [1],
      }
    },
    methods: {
      async fetchUserPage(pageNum) {
          const token = localStorage.getItem("token");
          if(token === null){
              alert("User not logged in");
              return;
          }
  
          const response = await fetch(`/backend/api/user/page/${pageNum}`, {
              method: "GET",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });
  
          const resData = await response.json();
          if(resData["content"].length > 0){
              this.users = resData.content
                console.log(resData.content)
              let pages = [];
  
              for(let i = 0; i < resData.size / 10; i++){
                  pages.push(i + 1);
              }
  
              this.pages = pages;
  
              return;
          }
  
          this.pages = [];
      },
      async changeStatus(id, status) {
        const token = localStorage.getItem("token");
          if(token === null){
              alert("User not logged in");
              return;
          }

          const data = {
            status
          }
  
          const response = await fetch(`/backend/api/user/update/status/${id}`, {
              method: "POST",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              },
              body: JSON.stringify(data)
          });

          const resData = await response.json();
          
          if(resData){
            alert("Success!");
            window.location.reload();
            return;
          }

          alert("Server side error");
          window.location.reload();
      },
      async changePage(pageNum) {
          await this.fetchUserPage(pageNum)
      },
      navigateToEditUser(id) {
          window.location = `/users/edit/${id}`
      },
      navigateToNewUser() {
          window.location = '/users/add'
      }
    }
  }
  </script>
  
  <style>
  .category-wrapper {
    justify-content: center;
    align-items: center;
  }
  
  .pages-select {
      margin: auto;
      margin-bottom: 50px;
      display: flex;
  }
  
  .page-num {
      font-size: 20px;
      font-weight: bold;
      padding: 5px 10px 5px 10px;
      margin: 5px;
  
      border-radius: 10px;
      border: 2px solid black;
  }
  
  .page-num:hover {
      cursor: pointer;
  
      background-color: black;
      border: 2px solid grey;
      color: white;
      transition: 200ms;
  }
  </style>
  