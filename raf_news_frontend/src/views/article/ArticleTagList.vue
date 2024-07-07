<template>
    <div class="container article-wrapper mt-5">
      <h1 class="text-center">{{this.tag.name}}</h1>
      <div class="mx-auto">
        <table class="table table-dark table-stripped mt-5">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">Title</th>
              <th scope="col">Content</th>
              <th scope="col">Category</th>
              <th scope="col">Created</th>
              <th scope="col">Views</th>
            </tr>
          </thead>
          <tbody>
              <tr v-for="(article, idx) in articles" :key="idx">
                  <th>{{ article.id }}</th>
                  <th><a @click="navigateToArticle(article.id)">{{ article.title }}</a></th>
                  <th>{{ article.text.slice(0, 30) }}...</th>
                  <th>{{ getCategory(article.categoryId) }}</th>
                  <th>{{ (new Date(article.createdAt)).toDateString() }}</th>
                  <th>{{ article.viewCnt }}</th>
              </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ArticleTagList',
    data() {
      return  {
          articles: [],
          categories: [],
          tag: {name: ''}
      }
    },
    methods: {
      async fetchTagArticles(id) {
          const token = localStorage.getItem("token");
          if(token === null){
              alert("User not logged in");
              return;
          }
          
          const response = await fetch(`/backend/api/article/tag/${id}`, {
              method: "GET",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });
          
          const resData = await response.json();
          
          if(resData.length > 0){
              this.articles = resData
              return;
          }
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
          
          if(resJson){
            this.categories = resJson;
          }
      },
      async fetchTag(id) {
        const token = localStorage.getItem("token");
          if(token === null){
              alert("User not logged in");
              return;
          }
  
          const response = await fetch(`/backend/api/tag/${id}`, {
              method: "GET",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });

          const resJson = await response.json();

          if(resJson.name){
            this.tag = resJson;
          }
      },
      getCategory(id){
        for(let i = 0; i < this.categories.length; i++){
            if(this.categories[i].id === id){
                return this.categories[i].name
            }
        }
      },
      navigateToArticle(id){
        window.open(`/articles/${id}`, "_blank")
      }
    },
    async mounted() {
      await this.fetchTagArticles(this.$route.params['id']);
      await this.fetchCategories();
      await this.fetchTag(this.$route.params['id']);
    },
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
  