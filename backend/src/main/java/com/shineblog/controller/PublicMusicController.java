package com.shineblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shineblog.common.ApiResponse;
import com.shineblog.common.PageResponse;
import com.shineblog.entity.MusicTrack;
import com.shineblog.service.MusicTrackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/music")
public class PublicMusicController {

    private final MusicTrackService musicTrackService;

    public PublicMusicController(MusicTrackService musicTrackService) {
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
                        .eq(MusicTrack::getStatus, "PUBLISHED")
                        .orderByDesc(MusicTrack::getSortOrder)
                        .orderByDesc(MusicTrack::getReleasedAt)
        );
        return ApiResponse.ok(new PageResponse<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }
}

