<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ArrowRight, Eye } from 'lucide-vue-next'
import { fetchPublishedArticles } from '../api/articles'
import type { Article } from '../types'

const articles = ref<Article[]>([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const page = await fetchPublishedArticles()
    articles.value = page.records
  } catch {
    error.value = '暂时无法加载文章'
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="view-page">
    <section class="page-head">
      <p class="eyebrow">Writing</p>
      <h1>文章</h1>
      <p>技术笔记、开发记录和偶尔冒出来的想法。</p>
    </section>

    <section class="content-band">
      <div v-if="loading" class="state">加载中...</div>
      <div v-else-if="error" class="state error">{{ error }}</div>
      <div v-else-if="articles.length === 0" class="state">还没有已发布文章</div>
      <div v-else class="article-list">
        <article v-for="article in articles" :key="article.id" class="article-card">
          <img v-if="article.coverUrl" class="article-cover" :src="article.coverUrl" :alt="article.title" />
          <div>
            <p class="date">{{ article.publishedAt?.slice(0, 10) || '未发布' }}</p>
            <h2>{{ article.title }}</h2>
            <p>{{ article.summary || '这篇文章暂时没有摘要。' }}</p>
          </div>
          <div class="card-footer">
            <span class="view-count">
              <Eye :size="16" />
              {{ article.viewCount || 0 }}
            </span>
            <RouterLink class="icon-link" :to="`/articles/${article.slug}`">
              阅读
              <ArrowRight :size="16" />
            </RouterLink>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>
