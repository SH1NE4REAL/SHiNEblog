# API 草图

后端默认地址：`http://localhost:8080`

## 公开接口

### 文章列表

`GET /api/articles?page=1&size=10`

返回已发布文章分页。

### 文章详情

`GET /api/articles/{slug}`

返回单篇已发布文章，并将浏览量加一。

### 项目列表

`GET /api/projects?page=1&size=20`

返回已发布项目。

### 音乐列表

`GET /api/music?page=1&size=20`

返回已发布音乐。当前阶段音频使用 `audioUrl`，后续再补文件上传。

## 后台接口

后台接口需要请求头：

```http
Authorization: Bearer <token>
```

### 登录

`POST /api/admin/auth/login`

```json
{
  "username": "admin",
  "password": "<your-admin-password>"
}
```

默认账号密码在 `backend/src/main/resources/application.yml` 中配置，推荐开发时用环境变量覆盖。

### 后台文章列表

`GET /api/admin/articles?page=1&size=20`

### 创建文章

`POST /api/admin/articles`

```json
{
  "title": "第一篇博客",
  "slug": "hello-shine-blog",
  "summary": "摘要",
  "coverUrl": "",
  "content": "# Markdown 内容",
  "status": "PUBLISHED",
  "categoryId": 1
}
```

### 更新文章

`PUT /api/admin/articles/{id}`

请求体同创建文章。

### 删除文章

`DELETE /api/admin/articles/{id}`

### 项目管理

`GET /api/admin/projects`

`POST /api/admin/projects`

`PUT /api/admin/projects/{id}`

`DELETE /api/admin/projects/{id}`

### 音乐管理

`GET /api/admin/music`

`POST /api/admin/music`

`PUT /api/admin/music/{id}`

`DELETE /api/admin/music/{id}`

### 文件上传

`POST /api/admin/uploads`

表单字段：

- `file`：上传文件
- `type`：`image` 或 `audio`

返回：

```json
{
  "success": true,
  "message": "ok",
  "data": {
    "url": "/uploads/image/2026/05/example.webp",
    "originalFilename": "cover.webp"
  }
}
```
