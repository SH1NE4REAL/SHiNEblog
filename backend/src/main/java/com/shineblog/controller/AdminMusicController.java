package com.shineblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shineblog.common.ApiResponse;
import com.shineblog.common.PageResponse;
import com.shineblog.dto.MusicTrackRequest;
import com.shineblog.entity.MusicTrack;
import com.shineblog.service.MusicTrackService;
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
@RequestMapping("/api/admin/music")
public class AdminMusicController {

    private final MusicTrackService musicTrackService;

    public AdminMusicController(MusicTrackService musicTrackService) {
        this.musicTrackService = musicTrackService;
    }

    @GetMapping
    public ApiResponse<PageResponse<MusicTrack>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "20") long size
    ) {
        Page<MusicTrack> result = musicTrackService.page(
                new Page<>(page, size),
                new LambdaQueryWrapper<MusicTrack>()
                        .orderByDesc(MusicTrack::getSortOrder)
                        .orderByDesc(MusicTrack::getReleasedAt)
        );
        return ApiResponse.ok(new PageResponse<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }

    @PostMapping
    public ApiResponse<MusicTrack> create(@Valid @RequestBody MusicTrackRequest request) {
        MusicTrack track = toTrack(request);
        track.setReleasedAt("PUBLISHED".equals(track.getStatus()) ? LocalDateTime.now() : null);
        track.setCreatedAt(LocalDateTime.now());
        track.setUpdatedAt(LocalDateTime.now());
        musicTrackService.save(track);
        return ApiResponse.ok(track);
    }

    @PutMapping("/{id}")
    public ApiResponse<MusicTrack> update(@PathVariable Long id, @Valid @RequestBody MusicTrackRequest request) {
        MusicTrack oldTrack = musicTrackService.getById(id);
        MusicTrack track = toTrack(request);
        track.setId(id);
        track.setReleasedAt(resolveReleasedAt(oldTrack, track));
        track.setUpdatedAt(LocalDateTime.now());
        musicTrackService.updateById(track);
        return ApiResponse.ok(track);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        musicTrackService.removeById(id);
        return ApiResponse.ok();
    }

    private MusicTrack toTrack(MusicTrackRequest request) {
        MusicTrack track = new MusicTrack();
        track.setTitle(request.title().trim());
        track.setSlug(resolveSlug(request.slug()));
        track.setArtist(blankToNull(request.artist()));
        track.setDescription(blankToNull(request.description()));
        track.setCoverUrl(blankToNull(request.coverUrl()));
        track.setAudioUrl(request.audioUrl().trim());
        track.setDurationSeconds(request.durationSeconds());
        track.setTags(blankToNull(request.tags()));
        track.setStatus(request.status() == null ? "DRAFT" : request.status());
        track.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        return track;
    }

    private LocalDateTime resolveReleasedAt(MusicTrack oldTrack, MusicTrack newTrack) {
        if (!"PUBLISHED".equals(newTrack.getStatus())) {
            return oldTrack == null ? null : oldTrack.getReleasedAt();
        }
        return oldTrack == null || oldTrack.getReleasedAt() == null ? LocalDateTime.now() : oldTrack.getReleasedAt();
    }

    private String resolveSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return "track-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return slug.trim();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

