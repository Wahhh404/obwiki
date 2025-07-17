import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: () => import('../views/admin/admin-category.vue')
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: () => import('../views/admin/admin-ebook.vue')
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: () => import('../views/admin/admin-doc.vue'),
    props: route => ({ ebookId: route.query.ebookId }) // 解析 ebookId 参数
  },
  {
    path: '/doc',
    name: 'Doc',
    component: () => import('../views/DocView.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: () => import('../views/admin/admin-user.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
