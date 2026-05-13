# SHiNE Blog

一个从零开始的个人博客项目，后端使用 Java/Spring Boot，前端使用 Vue/Vite，数据层使用 MySQL，缓存与后台登录 token 使用 Redis。

## 项目结构

```text
SHiNEblog/
  backend/   Spring Boot API
  frontend/  Vue 3 + Vite web app
  docs/      数据库脚本、接口和开发记录
```

## 本地启动

1. 创建 MySQL 数据库并执行 [docs/database.sql](docs/database.sql)。
2. 复制 `backend/application-local.example.yml` 为 `backend/application-local.yml`，填入你的 MySQL、Redis 和后台账号配置。`application-local.yml` 已被 Git 忽略，不要提交。
3. 启动后端：

```powershell
cd backend
mvn spring-boot:run
```

4. 启动前端：

```powershell
cd frontend
npm.cmd install
npm.cmd run dev
```

后端默认运行在 `http://localhost:8080`，前端默认运行在 `http://localhost:5173`。

后台入口不会显示在公开导航中，开发阶段隐藏路径为：

```text
http://localhost:5173/studio
```

## 上传文件

后台支持上传图片和音频：

- 图片：`jpg`、`jpeg`、`png`、`gif`、`webp`
- 音频：`mp3`、`wav`、`ogg`、`m4a`、`flac`

文件默认保存到后端启动目录下的 `uploads/`，并通过 `/uploads/...` 访问。开发时 Vite 已代理 `/uploads` 到后端。

## 背景音乐

右上角音乐按钮会播放：

```text
backend/uploads/bgm/bgm.mp3
```

当访客播放音乐页里的曲目时，背景音乐会自动暂停。公开部署前请确认背景音乐文件拥有可发布授权。

## 背景视频素材

非首页页面使用循环背景视频：

```text
backend/uploads/backgrounds/article-dive.mp4   文章页背景，镜像并循环头朝下片段
backend/uploads/backgrounds/ui-articles.mp4    项目等页面背景
backend/uploads/backgrounds/ui-music.mp4       音乐页背景
```
