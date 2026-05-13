package com.shineblog.controller;

import com.shineblog.common.ApiResponse;
import com.shineblog.config.UploadProperties;
import com.shineblog.dto.UploadResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/uploads")
public class AdminUploadController {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final Set<String> AUDIO_EXTENSIONS = Set.of("mp3", "wav", "ogg", "m4a", "flac");

    private final UploadProperties uploadProperties;

    public AdminUploadController(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @PostMapping
    public ApiResponse<UploadResponse> upload(
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "image") String type
    ) throws IOException {
        if (file.isEmpty()) {
            return new ApiResponse<>(false, "文件不能为空", null);
        }

        String originalFilename = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (!isAllowed(type, extension)) {
            return new ApiResponse<>(false, "不支持的文件类型", null);
        }

        LocalDate today = LocalDate.now();
        String folder = normalizeType(type) + "/" + today.getYear() + "/" + String.format("%02d", today.getMonthValue());
        Path uploadRoot = Path.of(uploadProperties.getDir()).toAbsolutePath().normalize();
        Path targetDir = uploadRoot.resolve(folder).normalize();
        Files.createDirectories(targetDir);

        String filename = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        Path targetFile = targetDir.resolve(filename).normalize();
        file.transferTo(targetFile);

        String url = "/uploads/" + folder + "/" + filename;
        return ApiResponse.ok(new UploadResponse(url, originalFilename));
    }

    private boolean isAllowed(String type, String extension) {
        if ("audio".equals(normalizeType(type))) {
            return AUDIO_EXTENSIONS.contains(extension);
        }
        return IMAGE_EXTENSIONS.contains(extension);
    }

    private String normalizeType(String type) {
        return "audio".equalsIgnoreCase(type) ? "audio" : "image";
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
