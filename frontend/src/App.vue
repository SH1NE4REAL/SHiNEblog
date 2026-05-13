<script setup lang="ts">
import { ArrowLeft, FileText, FolderKanban, Home, Music } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import BgmControl from './components/BgmControl.vue'
import IntroSplash from './components/IntroSplash.vue'
import RouteVideoBackdrop from './components/RouteVideoBackdrop.vue'

const router = useRouter()

function goBack() {
  if (window.history.length > 1) {
    router.back()
    return
  }
  router.push('/')
}
</script>

<template>
  <IntroSplash />
  <RouteVideoBackdrop />
  <div class="shell">
    <header class="topbar">
      <div class="brand-area">
        <button class="back-button" type="button" title="返回上一页" @click="goBack">
          <ArrowLeft :size="20" />
        </button>
        <RouterLink class="brand" to="/">
          <span>SHiNE Blog</span>
        </RouterLink>
      </div>
      <nav class="nav">
        <RouterLink to="/">
          <Home :size="18" />
          首页
        </RouterLink>
        <RouterLink to="/articles">
          <FileText :size="18" />
          文章
        </RouterLink>
        <RouterLink to="/projects">
          <FolderKanban :size="18" />
          项目
        </RouterLink>
        <RouterLink to="/music">
          <Music :size="18" />
          音乐
        </RouterLink>
      </nav>
      <BgmControl />
    </header>
    <main>
      <RouterView />
    </main>
  </div>
</template>
