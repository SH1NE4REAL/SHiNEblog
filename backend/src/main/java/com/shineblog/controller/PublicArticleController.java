package com.shineblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shineblog.common.ApiResponse;
import com.shineblog.common.PageResponse;
import com.shineblog.entity.Article;
import com.shineblog.service.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class PublicArticleController {

    private final ArticleService articleService;

    public PublicArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ApiResponse<PageResponse<Article>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size
    ) {
        Page<Article> result = articleService.page(
                new Page<>(page, size),
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, "PUBLISHED")
                        .orderByDesc(Article::getPublishedAt)
        );
        return ApiResponse.ok(new PageResponse<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }

    @GetMapping("/{slug}")
    public ApiResponse<Article> detail(@PathVariable String slug) {
        Article article = articleService.getOne(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getSlug, slug)
                        .eq(Article::getStatus, "PUBLISHED"),
                false
        );
        if (article == null) {
            return new ApiResponse<>(false, "文章不存在", null);
        }

        articleService.update(new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, article.getId())
                .setSql("view_count = view_count + 1"));
        article.setViewCount(article.getViewCount() + 1);
        return ApiResponse.ok(article);
    }
}
