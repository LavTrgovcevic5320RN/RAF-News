<template>
    <div class="container article-wrapper mt-5">
      <h1 class="text-center">Articles</h1>
      <div class="d-flex mx-auto mt-5">
        <button class="btn btn-primary mx-auto" @click="navigateToNewArticle()">New</button>
      </div>
      <div class="mx-auto">
        <table class="table table-dark table-stripped mt-5">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Title</th>
              <th scope="col">Content</th>
              <th scope="col">Category</th>
              <th scope="col">Created</th>
              <th scope="col">Edit</th>
              <th scope="col">Delete</th>
            </tr>
          </thead>
          <tbody>
              <tr v-for="(article, idx) in articles" :key="idx">
                  <th>{{ article.id }}</th>
                  <th><a @click="navigateToArticle(article.id)">{{ article.title }}</a></th>
                  <th>{{ article.text.slice(0, 30) }}...</th>
                  <th>{{ getCategory(article.categoryId) }}</th>
                  <th>{{ (new Date(article.createdAt)).toDateString() }}</th>
                  <th>
                      <button class="btn btn-primary" @click="navigateToEditArticle(article.id)">Edit</button>
                  </th>
                  <th>
                      <button class="btn btn-danger" @click="deleteArticle(article.id)">Delete</button>
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
    name: 'ArticleList',
    async mounted() {
      await this.fetchArticlePage(1);
      await this.fetchCategories();
    },
    data() {
      return  {
          articles: [],
          categories: [],
          pages: [1],
      }
    },
    methods: {
      async fetchArticlePage(pageNum) {
          const token = localStorage.getItem("token");
          if(token === null){
              alert("User not logged in");
              return;
          }
  
          const response = await fetch(`/backend/api/article/page/${pageNum}`, {
              method: "GET",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });
  
          const resData = await response.json();
          if(resData["content"].length > 0){
              this.articles = resData.content
                
              let pages = [];
  
              for(let i = 0; i < resData.size / 10; i++){
                  pages.push(i + 1);
              }
  
              this.pages = pages;
  
              return;
          }
  
          this.pages = [];
      },
      async changePage(pageNum) {
          await this.fetchArticlePage(pageNum)
      },
      async fetchCategories () {
        const token = localStorage.getItem("token");
          if(token === null){
              alert("User not logged in");
              return;
          }
  
          const response = await fetch(`/backend/api/category/all/`, {
              method: "GET",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });

          const resJson = await response.json();
          console.log(resJson);
          if(resJson){
            this.categories = resJson;
          }
      },
      async deleteArticle(id){
        const token = localStorage.getItem("token");
        if(token === null){
              alert("User not logged in");
              return;
          }
  
          const response = await fetch(`/backend/api/article/delete/${id}`, {
              method: "DELETE",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });

          if(response.status === 204){
            alert("Success");
            window.location.reload();
          }
      },
      getCategory(id){
        for(let i = 0; i < this.categories.length; i++){
            if(this.categories[i].id === id){
                return this.categories[i].name
            }
        }
      },
      navigateToEditArticle(id) {
          window.location = `/articles/edit/${id}`
      },
      navigateToNewArticle() {
          window.location = '/articles/add'
      },
      navigateToArticle(id){
        window.open(`/articles/${id}`, "_blank");
      }
    }
  }
  </script>
  
  <style>
  .article-wrapper {
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
  