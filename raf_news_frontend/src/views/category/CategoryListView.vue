<template>
  <div class="container category-wrapper mt-5">
    <h1 class="text-center">Categories</h1>
    <div class="d-flex mx-auto mt-5">
      <button class="btn btn-primary mx-auto" @click="navigateToNew()">New</button>
    </div>
    <div class="mx-auto">
      <table class="table table-dark table-stripped mt-5">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Edit</th>
            <th scope="col">Delete</th>
          </tr>
        </thead>
        <tbody>
            <tr v-for="(category, idx) in categories" :key="idx">
                <th>{{ category.id }}</th>
                <th><a @click="navigateToArticleCategory(category.id)">{{ category.name }}</a></th>
                <th>{{ category.description }}</th>
                <th>
                    <button class="btn btn-primary" @click="navigateToEdit(category.id)">Edit</button>
                </th>
                <th>
                    <button class="btn btn-danger" @click="deleteCategory(category.id)">Delete</button>
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
  name: 'CategoryList',
  async mounted() {
    await this.fetchCategoriesPage(1);
  },
  data() {
    return  {
        categories: [],
        pages: [1],
    }
  },
  methods: {
    async fetchCategoriesPage(pageNum) {
        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }

        const response = await fetch(`/backend/api/category/page/${pageNum}`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });

        const resData = await response.json();
        if(resData["content"].length > 0){
            this.categories = resData.content

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
        await this.fetchCategoriesPage(pageNum)
    },
    async deleteCategory(id){
        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }

        const response = await fetch(`/backend/api/category/delete/${id}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });

        if(response.status === 500){
            alert("Category is not empty");
            return;
        }

        if(response.status === 200){
            alert("Success!");
        }

        window.location.reload();
    }
    ,
    navigateToEdit(id) {
        window.location = `/categories/edit/${id}`
    },
    navigateToNew() {
        window.location = '/categories/add'
    },
    navigateToArticleCategory(id){
      window.location = `/articles/category/${id}`
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
