export type ApiResponse<T> = {
  success: boolean
  message: string
  data: T
}

export type PageResponse<T> = {
  records: T[]
  total: number
  current: number
  size: number
}

export type ArticleStatus = 'DRAFT' | 'PUBLISHED'

export type Article = {
  id?: number
  title: string
  slug: string
  summary?: string
  coverUrl?: string
  content: string
  status: ArticleStatus
  categoryId?: number
  viewCount?: number
  publishedAt?: string
  createdAt?: string
  updatedAt?: string
}

export type Project = {
  id?: number
  name: string
  slug: string
  summary?: string
  coverUrl?: string
  techStack?: string
  repoUrl?: string
  demoUrl?: string
  status: ArticleStatus
  sortOrder?: number
  createdAt?: string
  updatedAt?: string
}

export type MusicTrack = {
  id?: number
  title: string
  slug: string
  artist?: string
  description?: string
  coverUrl?: string
  audioUrl: string
  durationSeconds?: number
  tags?: string
  status: ArticleStatus
  sortOrder?: number
  releasedAt?: string
  createdAt?: string
  updatedAt?: string
}

export type LoginResponse = {
  token: string
  expiresInHours: number
}

export type UploadResult = {
  url: string
  originalFilename: string
}
