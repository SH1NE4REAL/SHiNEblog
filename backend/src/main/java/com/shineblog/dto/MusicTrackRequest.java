package com.shineblog.dto;

import jakarta.validation.constraints.NotBlank;

public record MusicTrackRequest(
        @NotBlank String title,
        String slug,
        String artist,
        String description,
        String coverUrl,
        @NotBlank String audioUrl,
        Integer durationSeconds,
        String tags,
        String status,
        Integer sortOrder
) {
}

