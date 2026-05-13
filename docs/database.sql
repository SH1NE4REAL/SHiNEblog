CREATE DATABASE IF NOT EXISTS shine_blog
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE shine_blog;

CREATE TABLE IF NOT EXISTS blog_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  slug VARCHAR(100) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_blog_category_slug (slug)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS blog_article (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(160) NOT NULL,
  slug VARCHAR(180) NOT NULL,
  summary VARCHAR(500),
  cover_url VARCHAR(500),
  content MEDIUMTEXT NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  category_id BIGINT,
  view_count BIGINT NOT NULL DEFAULT 0,
  published_at DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_blog_article_slug (slug),
  KEY idx_blog_article_status_published_at (status, published_at),
  KEY idx_blog_article_category_id (category_id),
  CONSTRAINT fk_blog_article_category
    FOREIGN KEY (category_id) REFERENCES blog_category (id)
    ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS blog_project (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(160) NOT NULL,
  slug VARCHAR(180) NOT NULL,
  summary VARCHAR(600),
  cover_url VARCHAR(500),
  tech_stack VARCHAR(300),
  repo_url VARCHAR(500),
  demo_url VARCHAR(500),
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  sort_order INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_blog_project_slug (slug),
  KEY idx_blog_project_status_sort (status, sort_order)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS blog_music_track (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(160) NOT NULL,
  slug VARCHAR(180) NOT NULL,
  artist VARCHAR(120),
  description VARCHAR(600),
  cover_url VARCHAR(500),
  audio_url VARCHAR(500) NOT NULL,
  duration_seconds INT,
  tags VARCHAR(300),
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
  sort_order INT NOT NULL DEFAULT 0,
  released_at DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_blog_music_track_slug (slug),
  KEY idx_blog_music_track_status_sort (status, sort_order, released_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
