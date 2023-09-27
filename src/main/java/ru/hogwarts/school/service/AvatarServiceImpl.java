package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Transactional
@RequiredArgsConstructor
@Service
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> new ElementNotExistException("Студент не найден"));
        Path filePath = Path.of(avatarsDir, studentId + "." +
                getExtension(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }
@Override
    public Optional<Avatar> findAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
