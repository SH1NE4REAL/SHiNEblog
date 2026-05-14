<script setup lang="ts">
import MarkdownIt from 'markdown-it'
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { fetchArticleBySlug, fetchPublishedArticles } from '../api/articles'
import type { Article } from '../types'

const route = useRoute()
const markdown = new MarkdownIt({ html: true, linkify: true, typographer: true })
const article = ref<Article | null>(null)
const articles = ref<Article[]>([])
const loading = ref(true)
const error = ref('')

const currentSlug = computed(() => String(route.params.slug || ''))
const html = computed(() => markdown.render(article.value?.content || ''))

async function loadArticle(slug: string) {
  loading.value = true
  error.value = ''
  try {
    article.value = await fetchArticleBySlug(slug)
  } catch {
    article.value = null
    error.value = '文章不存在或暂时不可访问'
  } finally {
    loading.value = false
  }
}

async function loadArticleList() {
  try {
    const articlePage = await fetchPublishedArticles(1, 50)
    articles.value = articlePage.records
  } catch {
    articles.value = []
  }
}

function scrollReaderToTop() {
  globalThis.requestAnimationFrame(() => {
    document.querySelector('.reader-panel')?.scrollIntoView({ block: 'start' })
  })
}

watch(
  currentSlug,
  (slug) => {
    if (slug) {
      loadArticle(slug)
    }
  },
  { immediate: true }
)

onMounted(loadArticleList)
</script>

<template>
  <div class="view-page">
    <section class="article-reader-layout">
      <aside class="article-sidebar">
        <p class="eyebrow">Article List</p>
        <h2>文章列表</h2>
        <div v-if="articles.length === 0" class="sidebar-empty">暂无文章</div>
        <RouterLink
          v-for="item in articles"
          :key="item.id"
          class="sidebar-article-link"
          :class="{ active: item.slug === article?.slug }"
          :to="`/articles/${item.slug}`"
          @click="scrollReaderToTop"
        >
          <span>{{ item.title }}</span>
          <small>{{ item.publishedAt?.slice(0, 10) || '未发布' }}</small>
        </RouterLink>
      </aside>

      <section class="reader-panel">
        <div v-if="loading" class="state">加载中...</div>
        <div v-else-if="error" class="state error">{{ error }}</div>
        <article v-else-if="article" class="reader-article">
          <p class="date">{{ article.publishedAt?.slice(0, 10) || '未发布' }}</p>
          <h1>{{ article.title }}</h1>
          <p v-if="article.summary" class="summary">{{ article.summary }}</p>
          <img v-if="article.coverUrl" class="reader-cover" :src="article.coverUrl" :alt="article.title" />
          <div class="markdown" v-html="html"></div>
        </article>
      </section>
    </section>
  </div>
</template>
