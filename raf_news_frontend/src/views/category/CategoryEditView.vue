<template>
    <div class="cat-edit-wrapper mt-5">
        <h1 class="text-center mb-5">Edit category</h1>
        <form class="w-25 mx-auto" @submit.prevent="editCategory()">
            <div class="form-group mb-3">
                <label class="mb-3">Name</label>
                <input class="form-control" v-model="name" required>
            </div>
            <div class="form-group mb-5">
                <label class="mb-3">Description</label>
                <input class="form-control" v-model="description" required>
            </div>
            <button class="btn btn-primary" type="submit">
                Submit
            </button>
        </form>
    </div>
</template>

<script>
export default {
  name: 'CategoryEditView',
  async mounted() {
    await this.fetchCategory(this.$route.params["id"]);
  },
  data() {
    return  {
        category: null,
        name: "",
        description: ""
    }
  },
  methods: {
    async fetchCategory(id) {
        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }

        const response = await fetch(`/backend/api/category/${id}`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });

        const resData = await response.json();
        
        if(resData.description !== null){
            this.category = resData;
            this.name = resData.name;
            this.description = resData.description;
        }
    },
    async editCategory() {
        const newCategory = {
            id: this.$route.params.id,
            name: this.name,
            description: this.description
        }

        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }


        const response = await fetch("/backend/api/category/update", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(newCategory)
        });

        if(response.status === 500){
            alert("Server side error");
            return
        }

        if(response.status === 200){
            alert("Success!");
        }

        const resData = await response.json();
    }
  }
}
</script>

<style>
.cat-edit-wrapper {
    justify-content: center;
    align-items: center;
}
</style>