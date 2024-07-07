import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import CategoryListView from '../views/category/CategoryListView.vue'
import CategoryEditView from '../views/category/CategoryEditView.vue'
import CategoryAddView from '../views/category/CategoryAddView.vue'
import UserListView from '../views/user/UserListView.vue'
import UserAddView from '../views/user/UserAddView.vue'
import UserEditView from '../views/user/UserEditView.vue'
import ArticleListView from '../views/article/ArticleListView.vue'
import ArticleView from '../views/article/ArticleView.vue';
import ArticlePopularView from '../views/article/ArticlePopularView.vue'
import ArticleCategoryList from '../views/article/ArticleCategoryList.vue'
import ArticleTagList from '../views/article/ArticleTagList.vue'
import ArticleAddView from '../views/article/ArticleAddView.vue'
import ArticleEditView from '../views/article/ArticleEditView.vue';
import jwt_decode from 'jwt-decode'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'ArticleList',
      component: ArticleListView
    },
    {
      path: '/articles/:id',
      name: 'ArticleView',
      component: ArticleView
    },
    {
      path: '/popular',
      name: 'ArticlePopular',
      component: ArticlePopularView
    },
    {
      path: '/articles/category/:id',
      name: 'ArticleCategoryList',
      component: ArticleCategoryList
    },
    {
      path: '/articles/tag/:id',
      name: 'ArticleTagList',
      component: ArticleTagList
    },
    {
      path: '/articles/add',
      name: 'ArticleAddView',
      component: ArticleAddView
    },
    {
      path: '/articles/edit/:id',
      name: 'ArticleEditView',
      component: ArticleEditView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/categories',
      name: 'categories',
      component: CategoryListView
    },
    {
      path: '/categories/edit/:id',
      name: 'CategoryEdit',
      component: CategoryEditView
    },
    {
      path: '/categories/add',
      name: 'CategoryAdd',
      component: CategoryAddView
    },
    {
      path: '/users',
      name: 'UserList',
      component: UserListView
    },
    {
      path: '/users/add',
      name: 'UserAdd',
      component: UserAddView
    },
    {
      path: '/users/edit/:id',
      name: 'UserEdit',
      component: UserEditView
    }
  ]
})

router.beforeEach((to, from) => {
  const token = localStorage.getItem('token')

  if (token === null && to.name !== 'login') {
    return { name: 'login' }
  }else if(token === null){
    return true;
  }
  const decodedToken = jwt_decode(token)

  if(to.path.includes("user") && decodedToken["role"] === "Creator"){
    return { name: 'home'}
  }
  
  return true;
})

export default router
