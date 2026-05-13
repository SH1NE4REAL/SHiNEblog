package com.shineblog.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank String name,
        String slug,
        String summary,
        String coverUrl,
        String techStack,
        String repoUrl,
        String demoUrl,
        String status,
        Integer sortOrder
) {
}

