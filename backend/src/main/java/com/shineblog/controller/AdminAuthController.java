package com.shineblog.controller;

import com.shineblog.common.ApiResponse;
import com.shineblog.config.AdminProperties;
import com.shineblog.dto.LoginRequest;
import com.shineblog.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private static final String REDIS_KEY_PREFIX = "shineblog:admin:token:";

    private final AdminProperties adminProperties;
    private final StringRedisTemplate redisTemplate;

    public AdminAuthController(AdminProperties adminProperties, StringRedisTemplate redisTemplate) {
        this.adminProperties = adminProperties;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        if (!adminProperties.getUsername().equals(request.username())
                || !adminProperties.getPassword().equals(request.password())) {
            return new ApiResponse<>(false, "用户名或密码错误", null);
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        long ttlHours = adminProperties.getTokenTtlHours();
        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + token, request.username(), Duration.ofHours(ttlHours));
        return ApiResponse.ok(new LoginResponse(token, ttlHours));
    }
}

