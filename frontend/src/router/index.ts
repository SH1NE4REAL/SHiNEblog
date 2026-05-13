import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ArticlesView from '../views/ArticlesView.vue'
import ArticleView from '../views/ArticleView.vue'
import ProjectsView from '../views/ProjectsView.vue'
import MusicView from '../views/MusicView.vue'
import AdminLoginView from '../views/AdminLoginView.vue'
import AdminDashboardView from '../views/AdminDashboardView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomeView },
    { path: '/articles', component: ArticlesView },
    { path: '/articles/:slug', component: ArticleView },
    { path: '/projects', component: ProjectsView },
    { path: '/music', component: MusicView },
    { path: '/studio/login', component: AdminLoginView },
    { path: '/studio', component: AdminDashboardView },
    { path: '/shine-studio/login', redirect: '/studio/login' },
    { path: '/shine-studio', redirect: '/studio' },
    { path: '/admin', redirect: '/' },
    { path: '/admin/login', redirect: '/' }
  ]
})

router.beforeEach((to) => {
  if (to.path === '/studio' && !localStorage.getItem('shine_admin_token')) {
    return '/studio/login'
  }
  return true
})

export default router
