<template>
    <div class="cat-new-wrapper mt-5">
        <h1 class="text-center mb-5">New article</h1>
        <form class="w-25 mx-auto" @submit.prevent="addArticle()">
            <div class="form-group mb-3">
                <label class="mb-3">Title</label>
                <input class="form-control" v-model="title" required>
            </div>
            <div class="form-group mb-5">
                <label class="mb-3">Category</label>
                <select class="form-select" v-model="category">
                    <option v-for="(cat, idx) in categories" :key="idx" :value="cat.id">{{ cat.name }}</option>
                </select>
            </div>
            <div class="form-group mb-5">
                <label class="mb-3">Text</label>
                <textarea class="form-control" :rows="3" v-model="text"></textarea>
            </div>
            <div class="form-group mb-5">
                <label class="mb-3">Tags</label>
                <div class="d-flex justify-content-between align-items-center">
                    <input class="form-control" v-model="tag" required>
                    <button type="button" class="btn btn-primary ms-5" @click="addNewTag(this.tag)">Add</button>
                </div>
                <ul class="mt-3">
                    <li v-for="(tg, idx) in addedTags" :key="idx">{{ tg.name }}</li>
                </ul>
            </div>
            <button class="btn btn-primary" type="submit">
                Submit
            </button>
        </form>
    </div>
</template>

<script>
import jwt_decode from 'jwt-decode'
export default {
  name: 'ArticleAddView',
  data() {
    return  {
        title: "",
        category: "",
        text: "",
        allTags: [],
        categories: [],
        newTags: [],
        existingTags: [],
        addedTags: [],
        tag: "",
        userName: ""

    }
  },
  methods: {
    async addArticle() {
        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }

        const createdTags = [];

        for(let i = 0; i < this.newTags.length; i++){
            const tagRes = await fetch('/backend/api/tag/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(this.newTags[i])
            });

            const tag = await tagRes.json();
            createdTags.push(tag);
        }

        let tagIds = [];

        for(let i = 0; i < createdTags.length; i++){
            tagIds.push(createdTags[i].id);
        }

        for(let i = 0 ; i < this.existingTags.length; i++){
            tagIds.push(this.existingTags[i].id);
        }

        const newArticle = {
            title: this.title,
            text: this.text,
            createdAt: Date.now(),
            viewCnt: 0,
            author: this.userName,
            categoryId: this.category,
            tagIds
        }

        const res = await fetch('/backend/api/article/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(newArticle)
        });

        if(res.status === 500){
            alert("Category with this name already exists!");
            return;
        }

        const resJson = await res.json();
        
        if(resJson["description"]){
            alert('Success!');
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
      async fetchAllTags(){
        const token = localStorage.getItem("token");
        if(token === null){
              alert("User not logged in");
              return;
          }
  
          const response = await fetch(`/backend/api/tag/all/`, {
              method: "GET",
              mode: "cors",
              headers: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearer " + token
              }
          });

          const resJson = await response.json();

          this.allTags = resJson;
      },
      addNewTag(){
        for(let i = 0; i < this.allTags.length; i++){
            if(this.allTags[i].name === this.tag){
                this.existingTags.push(this.allTags[i]);
                this.addedTags.push(this.allTags[i]);
                return;
            }
        }

        this.newTags.push({name: this.tag});
        this.addedTags.push({name: this.tag });
      }
  },
  async mounted(){
    const token = localStorage.getItem('token')

    if (token === null) {
      return
    }

    const decodedToken = jwt_decode(token)
    this.userRole = decodedToken.role
    this.userName = decodedToken.name

    await this.fetchCategories();
    await this.fetchAllTags();
  }
}
</script>

<style>
.cat-add-wrapper {
    justify-content: center;
    align-items: center;
}
</style>