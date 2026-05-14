import { http } from './http'
import { cachedPublicRequest, prefetchPublicRequest } from './cache'
import type { ApiResponse, Article, LoginResponse, MusicTrack, PageResponse, Project, UploadResult } from '../types'

export async function fetchPublishedArticles(page = 1, size = 10) {
  return cachedPublicRequest(`articles:${page}:${size}`, async () => {
    const { data } = await http.get<ApiResponse<PageResponse<Article>>>('/articles', {
      params: { page, size }
    })
    return data.data
  })
}

export async function fetchArticleBySlug(slug: string) {
  return cachedPublicRequest(`article:${slug}`, async () => {
    const { data } = await http.get<ApiResponse<Article>>(`/articles/${slug}`)
    return data.data
  })
}

export async function login(username: string, password: string) {
  const { data } = await http.post<ApiResponse<LoginResponse>>('/admin/auth/login', {
    username,
    password
  })
  return data
}

export async function fetchAdminArticles(page = 1, size = 20) {
  const { data } = await http.get<ApiResponse<PageResponse<Article>>>('/admin/articles', {
    params: { page, size }
  })
  return data.data
}

export async function saveArticle(article: Article) {
  const payload = {
    title: article.title,
    slug: article.slug,
    summary: article.summary,
    coverUrl: article.coverUrl,
    content: article.content,
    status: article.status,
    categoryId: article.categoryId
  }

  if (article.id) {
    const { data } = await http.put<ApiResponse<Article>>(`/admin/articles/${article.id}`, payload)
    if (!data.success) {
      throw new Error(data.message)
    }
    return data.data
  }

  const { data } = await http.post<ApiResponse<Article>>('/admin/articles', payload)
  if (!data.success) {
    throw new Error(data.message)
  }
  return data.data
}

export async function deleteArticle(id: number) {
  await http.delete(`/admin/articles/${id}`)
}

export async function fetchProjects(page = 1, size = 20) {
  return cachedPublicRequest(`projects:${page}:${size}`, async () => {
    const { data } = await http.get<ApiResponse<PageResponse<Project>>>('/projects', {
      params: { page, size }
    })
    return data.data
  })
}

export async function fetchMusicTracks(page = 1, size = 20) {
  return cachedPublicRequest(`music:${page}:${size}`, async () => {
    const { data } = await http.get<ApiResponse<PageResponse<MusicTrack>>>('/music', {
      params: { page, size }
    })
    return data.data
  })
}

export function prefetchPublicContent() {
  prefetchPublicRequest('articles:1:10', async () => {
    const { data } = await http.get<ApiResponse<PageResponse<Article>>>('/articles', {
      params: { page: 1, size: 10 }
    })
    return data.data
  })
  prefetchPublicRequest('projects:1:20', async () => {
    const { data } = await http.get<ApiResponse<PageResponse<Project>>>('/projects', {
      params: { page: 1, size: 20 }
    })
    return data.data
  })
  prefetchPublicRequest('music:1:20', async () => {
    const { data } = await http.get<ApiResponse<PageResponse<MusicTrack>>>('/music', {
      params: { page: 1, size: 20 }
    })
    return data.data
  })
}

export async function fetchAdminProjects(page = 1, size = 20) {
  const { data } = await http.get<ApiResponse<PageResponse<Project>>>('/admin/projects', {
    params: { page, size }
  })
  return data.data
}

export async function saveProject(project: Project) {
  const payload = {
    name: project.name,
    slug: project.slug,
    summary: project.summary,
    coverUrl: project.coverUrl,
    techStack: project.techStack,
    repoUrl: project.repoUrl,
    demoUrl: project.demoUrl,
    status: project.status,
    sortOrder: project.sortOrder
  }

  if (project.id) {
    const { data } = await http.put<ApiResponse<Project>>(`/admin/projects/${project.id}`, payload)
    if (!data.success) {
      throw new Error(data.message)
    }
    return data.data
  }

  const { data } = await http.post<ApiResponse<Project>>('/admin/projects', payload)
  if (!data.success) {
    throw new Error(data.message)
  }
  return data.data
}

export async function deleteProject(id: number) {
  await http.delete(`/admin/projects/${id}`)
}

export async function fetchAdminMusicTracks(page = 1, size = 20) {
  const { data } = await http.get<ApiResponse<PageResponse<MusicTrack>>>('/admin/music', {
    params: { page, size }
  })
  return data.data
}

export async function saveMusicTrack(track: MusicTrack) {
  const payload = {
    title: track.title,
    slug: track.slug,
    artist: track.artist,
    description: track.description,
    coverUrl: track.coverUrl,
    audioUrl: track.audioUrl,
    durationSeconds: track.durationSeconds,
    tags: track.tags,
    status: track.status,
    sortOrder: track.sortOrder
  }

  if (track.id) {
    const { data } = await http.put<ApiResponse<MusicTrack>>(`/admin/music/${track.id}`, payload)
    if (!data.success) {
      throw new Error(data.message)
    }
    return data.data
  }

  const { data } = await http.post<ApiResponse<MusicTrack>>('/admin/music', payload)
  if (!data.success) {
    throw new Error(data.message)
  }
  return data.data
}

export async function deleteMusicTrack(id: number) {
  await http.delete(`/admin/music/${id}`)
}

export async function uploadFile(file: File, type: 'image' | 'audio') {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', type)

  const { data } = await http.post<ApiResponse<UploadResult>>('/admin/uploads', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
  if (!data.success) {
    throw new Error(data.message)
  }
  return data.data
}
