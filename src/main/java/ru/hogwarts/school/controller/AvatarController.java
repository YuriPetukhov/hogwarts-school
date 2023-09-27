package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDTO;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatarPreview(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id)
                .orElseThrow(() -> new ElementNotExistException("Такого аватара нет в базе"));
        AvatarDTO avatarDTO = AvatarDTO.fromAvatar(avatar);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatarDTO.getMediaType()));
        headers.setContentLength(avatarDTO.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatarDTO.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id)
                .orElseThrow(() -> new ElementNotExistException("Такого аватара нет в базе"));
        AvatarDTO avatarDTO = AvatarDTO.fromAvatar(avatar);
        Path path = Path.of(avatarDTO.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatarDTO.getMediaType());
            response.setContentLength((int) avatarDTO.getFileSize());
            is.transferTo(os);
        }
    }
}
