package com.shineblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shineblog.common.ApiResponse;
import com.shineblog.common.PageResponse;
import com.shineblog.dto.ProjectRequest;
import com.shineblog.entity.Project;
import com.shineblog.service.ProjectService;
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
@RequestMapping("/api/admin/projects")
public class AdminProjectController {

    private final ProjectService projectService;

    public AdminProjectController(ProjectService projectService) {
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
                        .orderByDesc(Project::getSortOrder)
                        .orderByDesc(Project::getUpdatedAt)
        );
        return ApiResponse.ok(new PageResponse<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }

    @PostMapping
    public ApiResponse<Project> create(@Valid @RequestBody ProjectRequest request) {
        Project project = toProject(request);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        projectService.save(project);
        return ApiResponse.ok(project);
    }

    @PutMapping("/{id}")
    public ApiResponse<Project> update(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        Project project = toProject(request);
        project.setId(id);
        project.setUpdatedAt(LocalDateTime.now());
        projectService.updateById(project);
        return ApiResponse.ok(project);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        projectService.removeById(id);
        return ApiResponse.ok();
    }

    private Project toProject(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.name().trim());
        project.setSlug(resolveSlug(request.slug()));
        project.setSummary(blankToNull(request.summary()));
        project.setCoverUrl(blankToNull(request.coverUrl()));
        project.setTechStack(blankToNull(request.techStack()));
        project.setRepoUrl(blankToNull(request.repoUrl()));
        project.setDemoUrl(blankToNull(request.demoUrl()));
        project.setStatus(request.status() == null ? "DRAFT" : request.status());
        project.setSortOrder(request.sortOrder() == null ? 0 : request.sortOrder());
        return project;
    }

    private String resolveSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            return "project-" + UUID.randomUUID().toString().substring(0, 8);
        }
        return slug.trim();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

