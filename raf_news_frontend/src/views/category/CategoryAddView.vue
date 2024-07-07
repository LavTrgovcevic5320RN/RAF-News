<template>
    <div class="cat-new-wrapper mt-5">
        <h1 class="text-center mb-5">New category</h1>
        <form class="w-25 mx-auto" @submit.prevent="addCategory()">
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
  name: 'CategoryAddView',
  data() {
    return  {
        name: "",
        description: ""
    }
  },
  methods: {
    async addCategory() {
        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }

        const res = await fetch('/backend/api/category/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                name: this.name, 
                description: this.description
            })
        });

        if(res.status === 500){
            alert("Category with this name already exists!");
            return;
        }

        const resJson = await res.json();

        if(resJson["description"]){
            alert('Success!');
        }
    }
  }
}
</script>

<style>
.cat-add-wrapper {
    justify-content: center;
    align-items: center;
}
</style>