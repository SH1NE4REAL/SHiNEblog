package com.shineblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shineblog.common.ApiResponse;
import com.shineblog.common.PageResponse;
import com.shineblog.entity.Project;
import com.shineblog.service.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class PublicProjectController {

    private final ProjectService projectService;

    public PublicProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ApiResponse<PageResponse<Project>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "20") long size
    ) {
        Page<Project> result = projectService.page(
                new Page<>(page, size),
                new LambdaQueryWrapper<Project>()
                        .eq(Project::getStatus, "PUBLISHED")
                        .orderByDesc(Project::getSortOrder)
                        .orderByDesc(Project::getUpdatedAt)
        );
        return ApiResponse.ok(new PageResponse<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }
}

