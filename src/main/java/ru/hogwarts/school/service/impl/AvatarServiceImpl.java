package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@RequiredArgsConstructor
@Transactional
@Service
public class AvatarServiceImpl implements AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar");
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> {
                    logger.error("No student found with id = " + studentId);
                    throw new ElementNotExistException("Студент не найден");
                });
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
        logger.info("Was invoked method for find avatar");
        return avatarRepository.findByStudentId(studentId);
    }

    @Override
    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for get all avatars");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    private String getExtension(String fileName) {
        logger.debug("Getting extension from filename");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
