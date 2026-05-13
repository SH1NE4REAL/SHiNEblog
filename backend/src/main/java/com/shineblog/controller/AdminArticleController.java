package com.shineblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shineblog.common.ApiResponse;
import com.shineblog.common.PageResponse;
import com.shineblog.dto.ArticleRequest;
import com.shineblog.entity.Article;
import com.shineblog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    private final ArticleService articleService;

    public AdminArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ApiResponse<PageResponse<Article>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size
    ) {
        Page<Article> result = articleService.page(
                new Page<>(page, size),
                new LambdaQueryWrapper<Article>().orderByDesc(Article::getUpdatedAt)
        );
        return ApiResponse.ok(new PageResponse<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }

    @GetMapping("/{id}")
    public ApiResponse<Article> detail(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            return new ApiResponse<>(false, "文章不存在", null);
        }
        return ApiResponse.ok(article);
    }

    @PostMapping
    public ApiResponse<Article> create(@Valid @RequestBody ArticleRequest request) {
        Article article = toArticle(request);
        article.setViewCount(0L);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        if ("PUBLISHED".equals(article.getStatus())) {
            article.setPublishedAt(LocalDateTime.now());
        }
        articleService.save(article);
        return ApiResponse.ok(article);
    }

    @PutMapping("/{id}")
    public ApiResponse<Article> update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        Article article = articleService.getById(id);
        if (article == null) {
            return new ApiResponse<>(false, "文章不存在", null);
        }
        Article updated = toArticle(request);
        updated.setId(id);
        updated.setViewCount(article.getViewCount());
        updated.setCreatedAt(article.getCreatedAt());
        updated.setUpdatedAt(LocalDateTime.now());
        updated.setPublishedAt(resolvePublishedAt(article, updated));
        articleService.updateById(updated);
        return ApiResponse.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ApiResponse.ok();
    }

    private Article toArticle(ArticleRequest request) {
        Article article = new Article();
        article.setTitle(request.title().trim());
        article.setSlug(resolveSlug(request.slug()));
        article.setSummary(blankToNull(request.summary()));
        article.setCoverUrl(blankToNull(request.coverUrl()));
        article.setContent(request.content());
        article.setStatus(request.status() == null ? "DRAFT" : request.status());
        article.setCategoryId(request.categoryId());
        return article;
    }

    private String resolveSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return "post-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return slug.trim();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private LocalDateTime resolvePublishedAt(Article oldArticle, Article newArticle) {
        if (!"PUBLISHED".equals(newArticle.getStatus())) {
            return oldArticle.getPublishedAt();
        }
        return oldArticle.getPublishedAt() == null ? LocalDateTime.now() : oldArticle.getPublishedAt();
    }
}
