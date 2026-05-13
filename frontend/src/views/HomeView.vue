<script setup lang="ts">
import { ArrowRight, FileText, FolderKanban, Music2 } from 'lucide-vue-next'
</script>

<template>
  <div class="view-page">
    <section class="home-hero">
      <div class="sink-lines" aria-hidden="true">
        <span></span>
        <span></span>
        <span></span>
        <span></span>
      </div>
      <div class="deep-silhouette" aria-hidden="true">
        <span class="head"></span>
        <span class="body"></span>
        <span class="limb left"></span>
        <span class="limb right"></span>
      </div>
      <p class="eyebrow">Personal Archive</p>
      <div class="hero-title-plate">
        <h1 class="welcome-title" aria-label="WELCOME TO SHINE'S BLOG">
          <span class="typewriter-line first">WELCOME TO</span>
          <span class="typewriter-line second">SHINE'S BLOG</span>
        </h1>
        <p class="hero-copy">Articles, projects, and music collected in one place.</p>
      </div>
    </section>

    <section class="content-band home-sections">
      <RouterLink class="section-tile" to="/articles">
        <FileText :size="24" />
        <span>
          <strong>文章</strong>
          <small>技术笔记、开发记录和想法</small>
        </span>
        <ArrowRight :size="18" />
      </RouterLink>
      <RouterLink class="section-tile" to="/projects">
        <FolderKanban :size="24" />
        <span>
          <strong>个人项目展示</strong>
          <small>作品、链接、技术栈和过程</small>
        </span>
        <ArrowRight :size="18" />
      </RouterLink>
      <RouterLink class="section-tile" to="/music">
        <Music2 :size="24" />
        <span>
          <strong>音乐</strong>
          <small>原创曲目和灵感片段</small>
        </span>
        <ArrowRight :size="18" />
      </RouterLink>
    </section>
  </div>
</template>
