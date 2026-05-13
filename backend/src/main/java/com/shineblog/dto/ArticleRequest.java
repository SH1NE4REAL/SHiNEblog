package com.shineblog.dto;

import jakarta.validation.constraints.NotBlank;

public record ArticleRequest(
        @NotBlank String title,
        String slug,
        String summary,
        String coverUrl,
        @NotBlank String content,
        String status,
        Long categoryId
) {
}
