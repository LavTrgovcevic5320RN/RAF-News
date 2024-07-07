<template>
    <div class="article-view-wrapper p-5 mt-5">
        <h1 class="text-center">
            {{ this.article.title }}
        </h1>
        <div class="d-flex justify-content-between">
            <h3>{{ this.article.author }}</h3>
            <h3>{{ ( new Date(this.article.createdAt)).toDateString() }}</h3>
        </div>
        <div class="article-text text-center mt-5 mb-5">
            {{ article.text }}
        </div>
        <h5>
           Views: {{ article.viewCnt }}
        </h5>
        <div class="d-flex mt-5">
            <div class="comments-container mx-auto mt-5">
                <h3 class="text-center">Comments</h3>
                <form class="mt-5 mb-5" @submit.prevent="addComment()">
                    <div class="form-group mb-3">
                        <label>Name</label>
                        <input
                        type="text"
                        class="form-control"
                        placeholder="Enter your name"
                        v-model="commentAuthor"
                        required
                        />
                    </div>
                    <div class="form-group">
                        <label>Comment</label>
                        <input
                        type="text"
                        class="form-control"
                        placeholder="Enter your comment"
                        v-model="commentText"
                        required
                        />
                    </div>
                    <button class="btn btn-primary mt-3" type="submit">Comment</button>
                </form>
                <div class="card comment" v-for="(comment, idx) in comments" :key="idx">
                    <div class="card-body">
                        <h5 class="card-title">{{ comment.author }}</h5>
                        <h6 class="card-subtitle mb-3">{{ (new Date(comment.createdAt)).toDateString() }}</h6>
                        <p class="card-text">
                            {{ comment.text }}
                        </p>
                    </div>
                </div>
            </div>
            <div class="tag-container mx-auto mt-5">
                <h3 class="text-center">Tags</h3>
                <div class="card tag" v-for="(tag, idx) in tags" :key="idx" @click="navigateToArticleTag(tag.id)">
                    <div class="card-body">
                        <h5 class="card-title text-center">{{ tag.name }}</h5>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  name: 'ArticleView',
  async mounted() {
    await this.increaseViewCnt(this.$route.params['id']);
    await this.fetchArticle(this.$route.params['id']);
    await this.fetchComments(this.$route.params['id']);
    await this.fetchTags(this.$route.params['id']);
  },
  data() {
    return  {
        article: '',
        comments: [],
        tags: [],
        commentAuthor: '',
        commentText: ''
    }
  },
  methods: {
    async fetchArticle(id){
        const token = localStorage.getItem("token");
        if(token === null){
            alert("User not logged in");
            return;
        }

        const response = await fetch(`/backend/api/article/${id}`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });

        const resJson = await response.json();
        if(resJson.title){
        this.article = resJson;
        }
    },
    async fetchComments(id){
        const token = localStorage.getItem("token");
        if(token === null){
              alert("User not logged in");
              return;
        }
  
        const response = await fetch(`/backend/api/comment/article/${id}`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });

        const resJson = await response.json();
        
        if(resJson.length > 0){
        this.comments = resJson;
        }
    },
    async fetchTags(id){
        const token = localStorage.getItem("token");
        if(token === null){
              alert("User not logged in");
              return;
        }
  
        const response = await fetch(`/backend/api/tag/article/${id}`, {
            method: "GET",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        });

        const resJson = await response.json();
        
        if(resJson.length > 0){
        this.tags = resJson;
        }
    },
    async addComment() {
        const token = localStorage.getItem("token");
        if(token === null){
              alert("User not logged in");
              return;
        }

        const newComment = {
            author: this.commentAuthor,
            text: this.commentText,
            createdAt: Date.now(),
            articleId: this.$route.params['id']
        }
  
        const response = await fetch(`/backend/api/comment/add/`, {
            method: "POST",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(newComment)
        });

        const resJson = await response.json();
        console.log(resJson);
    },
    async increaseViewCnt(id){
        const token = localStorage.getItem("token");
        if(token === null){
              alert("User not logged in");
              return;
        }

        const response = await fetch(`/backend/api/article/view/${id}`, {
            method: "PATCH",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
        })
    },
    navigateToArticleTag(id){
        window.location = `/articles/tag/${id}`;
    }
  }
}
</script>

<style>
.article-view-wrapper {
    justify-content: center;
    align-items: center;
}

.article-text{
    font-size: 25px;
    font-weight: 400;
}

.comments-container {
    overflow: auto;
}

.comment {
    margin: 20px;
}

.tag {
    margin: 20px;
}

.tag:hover {
    cursor: pointer;
}
</style>