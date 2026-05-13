<script setup lang="ts">
import axios from 'axios'
import { onMounted, reactive, ref } from 'vue'
import { Disc3, FileText, FolderKanban, Plus, Save, Trash2, Upload } from 'lucide-vue-next'
import {
  deleteArticle,
  deleteMusicTrack,
  deleteProject,
  fetchAdminArticles,
  fetchAdminMusicTracks,
  fetchAdminProjects,
  saveArticle,
  saveMusicTrack,
  saveProject,
  uploadFile
} from '../api/articles'
import type { ApiResponse, Article, MusicTrack, Project } from '../types'

type AdminTab = 'articles' | 'projects' | 'music'

const activeTab = ref<AdminTab>('articles')
const articles = ref<Article[]>([])
const projects = ref<Project[]>([])
const tracks = ref<MusicTrack[]>([])
const saving = ref(false)
const message = ref('')

const articleForm = reactive<Article>({
  title: '',
  slug: '',
  summary: '',
  coverUrl: '',
  content: '',
  status: 'DRAFT'
})

const projectForm = reactive<Project>({
  name: '',
  slug: '',
  summary: '',
  coverUrl: '',
  techStack: '',
  repoUrl: '',
  demoUrl: '',
  status: 'DRAFT',
  sortOrder: 0
})

const musicForm = reactive<MusicTrack>({
  title: '',
  slug: '',
  artist: 'SHiNE',
  description: '',
  coverUrl: '',
  audioUrl: '',
  durationSeconds: undefined,
  tags: '',
  status: 'DRAFT',
  sortOrder: 0
})

async function loadAll() {
  const [articlePage, projectPage, musicPage] = await Promise.all([
    fetchAdminArticles(),
    fetchAdminProjects(),
    fetchAdminMusicTracks()
  ])
  articles.value = articlePage.records
  projects.value = projectPage.records
  tracks.value = musicPage.records
}

function switchTab(tab: AdminTab) {
  activeTab.value = tab
  message.value = ''
}

function editArticle(article: Article) {
  Object.assign(articleForm, article)
}

function editProject(project: Project) {
  Object.assign(projectForm, project)
}

function editTrack(track: MusicTrack) {
  Object.assign(musicForm, track)
}

function newArticle() {
  Object.assign(articleForm, {
    id: undefined,
    title: '',
    slug: '',
    summary: '',
    coverUrl: '',
    content: '',
    status: 'DRAFT'
  })
}

function newProject() {
  Object.assign(projectForm, {
    id: undefined,
    name: '',
    slug: '',
    summary: '',
    coverUrl: '',
    techStack: '',
    repoUrl: '',
    demoUrl: '',
    status: 'DRAFT',
    sortOrder: 0
  })
}

function newTrack() {
  Object.assign(musicForm, {
    id: undefined,
    title: '',
    slug: '',
    artist: 'SHiNE',
    description: '',
    coverUrl: '',
    audioUrl: '',
    durationSeconds: undefined,
    tags: '',
    status: 'DRAFT',
    sortOrder: 0
  })
}

async function submitArticle() {
  saving.value = true
  message.value = ''
  if (!articleForm.slug.trim()) {
    articleForm.slug = makeSlug(articleForm.title, 'post')
  }
  try {
    const saved = await saveArticle(articleForm)
    Object.assign(articleForm, saved)
    await loadAll()
    message.value = '文章已保存'
  } catch (error) {
    message.value = getErrorMessage(error)
  } finally {
    saving.value = false
  }
}

async function submitProject() {
  saving.value = true
  message.value = ''
  if (!projectForm.slug.trim()) {
    projectForm.slug = makeSlug(projectForm.name, 'project')
  }
  try {
    const saved = await saveProject(projectForm)
    Object.assign(projectForm, saved)
    await loadAll()
    message.value = '项目已保存'
  } catch (error) {
    message.value = getErrorMessage(error)
  } finally {
    saving.value = false
  }
}

async function submitTrack() {
  saving.value = true
  message.value = ''
  if (!musicForm.slug.trim()) {
    musicForm.slug = makeSlug(musicForm.title, 'track')
  }
  try {
    const saved = await saveMusicTrack(musicForm)
    Object.assign(musicForm, saved)
    await loadAll()
    message.value = '音乐已保存'
  } catch (error) {
    message.value = getErrorMessage(error)
  } finally {
    saving.value = false
  }
}

async function removeArticle(id?: number) {
  if (!id) return
  await deleteArticle(id)
  await loadAll()
  newArticle()
}

async function removeProject(id?: number) {
  if (!id) return
  await deleteProject(id)
  await loadAll()
  newProject()
}

async function removeTrack(id?: number) {
  if (!id) return
  await deleteMusicTrack(id)
  await loadAll()
  newTrack()
}

async function uploadInto(
  event: Event,
  type: 'image' | 'audio',
  applyUrl: (url: string) => void
) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (!file) {
    return
  }
  message.value = '上传中...'
  try {
    const result = await uploadFile(file, type)
    applyUrl(result.url)
    message.value = `已上传：${result.originalFilename}`
  } catch (error) {
    message.value = getErrorMessage(error)
  } finally {
    input.value = ''
  }
}

function makeSlug(value: string, fallback: string) {
  const base = value
    .trim()
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-|-$/g, '')
  return base || `${fallback}-${Date.now()}`
}

function getErrorMessage(error: unknown) {
  if (axios.isAxiosError(error)) {
    const response = error.response?.data as ApiResponse<unknown> | undefined
    return response?.message || '保存失败'
  }
  if (error instanceof Error) {
    return error.message
  }
  return '保存失败'
}

onMounted(loadAll)
</script>

<template>
  <section class="admin-layout">
    <aside class="panel article-manager">
      <div class="panel-title">
        <h1>工作室</h1>
      </div>

      <div class="admin-tabs">
        <button :class="{ active: activeTab === 'articles' }" type="button" @click="switchTab('articles')">
          <FileText :size="17" />
          文章
        </button>
        <button :class="{ active: activeTab === 'projects' }" type="button" @click="switchTab('projects')">
          <FolderKanban :size="17" />
          项目
        </button>
        <button :class="{ active: activeTab === 'music' }" type="button" @click="switchTab('music')">
          <Disc3 :size="17" />
          音乐
        </button>
      </div>

      <div v-if="activeTab === 'articles'" class="admin-list">
        <button class="icon-button wide" type="button" @click="newArticle">
          <Plus :size="18" />
          新文章
        </button>
        <button v-for="article in articles" :key="article.id" class="article-row" type="button" @click="editArticle(article)">
          <span>{{ article.title }}</span>
          <small>{{ article.status }}</small>
        </button>
      </div>

      <div v-if="activeTab === 'projects'" class="admin-list">
        <button class="icon-button wide" type="button" @click="newProject">
          <Plus :size="18" />
          新项目
        </button>
        <button v-for="project in projects" :key="project.id" class="article-row" type="button" @click="editProject(project)">
          <span>{{ project.name }}</span>
          <small>{{ project.status }}</small>
        </button>
      </div>

      <div v-if="activeTab === 'music'" class="admin-list">
        <button class="icon-button wide" type="button" @click="newTrack">
          <Plus :size="18" />
          新音乐
        </button>
        <button v-for="track in tracks" :key="track.id" class="article-row" type="button" @click="editTrack(track)">
          <span>{{ track.title }}</span>
          <small>{{ track.status }}</small>
        </button>
      </div>
    </aside>

    <form v-if="activeTab === 'articles'" class="panel editor" @submit.prevent="submitArticle">
      <div class="editor-grid">
        <label class="field">
          <span>标题</span>
          <input v-model="articleForm.title" required />
        </label>
        <label class="field">
          <span>访问路径</span>
          <input v-model="articleForm.slug" placeholder="留空会自动生成" />
          <small>用于文章地址，例如 /articles/hello-shine-blog</small>
        </label>
      </div>
      <label class="field">
        <span>摘要</span>
        <input v-model="articleForm.summary" placeholder="可选，显示在文章列表" />
      </label>
      <label class="field">
        <span>封面地址</span>
        <input v-model="articleForm.coverUrl" placeholder="可选，图片 URL" />
        <span class="upload-line">
          <input
            type="file"
            accept="image/*"
            @change="uploadInto($event, 'image', (url) => (articleForm.coverUrl = url))"
          />
          <Upload :size="16" />
          选择封面图片
        </span>
      </label>
      <label class="field">
        <span>状态</span>
        <select v-model="articleForm.status">
          <option value="DRAFT">草稿</option>
          <option value="PUBLISHED">发布</option>
        </select>
      </label>
      <label class="field">
        <span>内容</span>
        <textarea v-model="articleForm.content" required placeholder="支持 Markdown，例如 # 标题"></textarea>
      </label>
      <div class="editor-actions">
        <p class="save-message">{{ message }}</p>
        <button v-if="articleForm.id" class="danger-button" type="button" @click="removeArticle(articleForm.id)">
          <Trash2 :size="18" />
          删除
        </button>
        <button class="primary-button" type="submit" :disabled="saving">
          <Save :size="18" />
          {{ saving ? '保存中' : '保存' }}
        </button>
      </div>
    </form>

    <form v-if="activeTab === 'projects'" class="panel editor" @submit.prevent="submitProject">
      <div class="editor-grid">
        <label class="field">
          <span>项目名</span>
          <input v-model="projectForm.name" required />
        </label>
        <label class="field">
          <span>访问路径</span>
          <input v-model="projectForm.slug" placeholder="留空会自动生成" />
        </label>
      </div>
      <label class="field">
        <span>简介</span>
        <input v-model="projectForm.summary" placeholder="项目一句话说明" />
      </label>
      <label class="field">
        <span>技术栈</span>
        <input v-model="projectForm.techStack" placeholder="Spring Boot, Vue, MySQL" />
      </label>
      <div class="editor-grid">
        <label class="field">
          <span>仓库链接</span>
          <input v-model="projectForm.repoUrl" />
        </label>
        <label class="field">
          <span>预览链接</span>
          <input v-model="projectForm.demoUrl" />
        </label>
      </div>
      <label class="field">
        <span>封面地址</span>
        <input v-model="projectForm.coverUrl" placeholder="可选，图片 URL" />
        <span class="upload-line">
          <input
            type="file"
            accept="image/*"
            @change="uploadInto($event, 'image', (url) => (projectForm.coverUrl = url))"
          />
          <Upload :size="16" />
          选择项目封面
        </span>
      </label>
      <div class="editor-grid">
        <label class="field">
          <span>状态</span>
          <select v-model="projectForm.status">
            <option value="DRAFT">草稿</option>
            <option value="PUBLISHED">发布</option>
          </select>
        </label>
        <label class="field">
          <span>排序</span>
          <input v-model.number="projectForm.sortOrder" type="number" />
        </label>
      </div>
      <div class="editor-actions">
        <p class="save-message">{{ message }}</p>
        <button v-if="projectForm.id" class="danger-button" type="button" @click="removeProject(projectForm.id)">
          <Trash2 :size="18" />
          删除
        </button>
        <button class="primary-button" type="submit" :disabled="saving">
          <Save :size="18" />
          {{ saving ? '保存中' : '保存' }}
        </button>
      </div>
    </form>

    <form v-if="activeTab === 'music'" class="panel editor" @submit.prevent="submitTrack">
      <div class="editor-grid">
        <label class="field">
          <span>曲名</span>
          <input v-model="musicForm.title" required />
        </label>
        <label class="field">
          <span>访问路径</span>
          <input v-model="musicForm.slug" placeholder="留空会自动生成" />
        </label>
      </div>
      <label class="field">
        <span>作者</span>
        <input v-model="musicForm.artist" />
      </label>
      <label class="field">
        <span>说明</span>
        <input v-model="musicForm.description" placeholder="创作说明、风格或灵感" />
      </label>
      <label class="field">
        <span>音频地址</span>
        <input v-model="musicForm.audioUrl" required placeholder="填写音频 URL，或直接上传音频文件" />
        <span class="upload-line">
          <input
            type="file"
            accept="audio/*"
            @change="uploadInto($event, 'audio', (url) => (musicForm.audioUrl = url))"
          />
          <Upload :size="16" />
          选择音频文件
        </span>
      </label>
      <label class="field">
        <span>封面地址</span>
        <input v-model="musicForm.coverUrl" placeholder="可选，图片 URL" />
        <span class="upload-line">
          <input
            type="file"
            accept="image/*"
            @change="uploadInto($event, 'image', (url) => (musicForm.coverUrl = url))"
          />
          <Upload :size="16" />
          选择音乐封面
        </span>
      </label>
      <div class="editor-grid">
        <label class="field">
          <span>标签</span>
          <input v-model="musicForm.tags" placeholder="Original, Piano, Demo" />
        </label>
        <label class="field">
          <span>排序</span>
          <input v-model.number="musicForm.sortOrder" type="number" />
        </label>
      </div>
      <label class="field">
        <span>状态</span>
        <select v-model="musicForm.status">
          <option value="DRAFT">草稿</option>
          <option value="PUBLISHED">发布</option>
        </select>
      </label>
      <div class="editor-actions">
        <p class="save-message">{{ message }}</p>
        <button v-if="musicForm.id" class="danger-button" type="button" @click="removeTrack(musicForm.id)">
          <Trash2 :size="18" />
          删除
        </button>
        <button class="primary-button" type="submit" :disabled="saving">
          <Save :size="18" />
          {{ saving ? '保存中' : '保存' }}
        </button>
      </div>
    </form>
  </section>
</template>
