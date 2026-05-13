package com.shineblog.dto;

public record LoginResponse(String token, long expiresInHours) {
}

