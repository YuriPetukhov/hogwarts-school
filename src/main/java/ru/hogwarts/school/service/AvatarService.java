package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;
    Optional<Avatar> findAvatar(Long studentId);
    List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize);
}
