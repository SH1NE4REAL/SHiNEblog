<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ExternalLink, Github } from 'lucide-vue-next'
import { fetchProjects } from '../api/articles'
import type { Project } from '../types'

const projects = ref<Project[]>([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const page = await fetchProjects()
    projects.value = page.records
  } catch {
    error.value = '暂时无法加载项目'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="view-page">
    <section class="page-head">
      <p class="eyebrow">Projects</p>
      <h1>个人项目展示</h1>
      <p>把正在做、做完了、以及值得回头看的东西放在这里。</p>
    </section>

    <section class="content-band">
      <div v-if="loading" class="state">加载中...</div>
      <div v-else-if="error" class="state error">{{ error }}</div>
      <div v-else-if="projects.length === 0" class="state">还没有公开项目</div>
      <div v-else class="project-grid">
        <article v-for="project in projects" :key="project.id" class="project-card">
          <div v-if="project.coverUrl" class="project-cover">
            <img :src="project.coverUrl" :alt="project.name" />
          </div>
          <div class="project-body">
            <p class="date">{{ project.techStack || 'Project' }}</p>
            <h2>{{ project.name }}</h2>
            <p>{{ project.summary || '这个项目还没有简介。' }}</p>
            <div class="link-row">
              <a v-if="project.repoUrl" :href="project.repoUrl" target="_blank" rel="noreferrer">
                <Github :size="16" />
                仓库
              </a>
              <a v-if="project.demoUrl" :href="project.demoUrl" target="_blank" rel="noreferrer">
                <ExternalLink :size="16" />
                预览
              </a>
            </div>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>
