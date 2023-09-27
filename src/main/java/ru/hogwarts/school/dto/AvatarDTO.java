package ru.hogwarts.school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvatarDTO {
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;
    private StudentDTO student;

    public static AvatarDTO fromAvatar(ru.hogwarts.school.model.Avatar avatar) {
        if (avatar == null) {
            return null;
        }

        AvatarDTO dto = new AvatarDTO();
        dto.setId(avatar.getId());
        dto.setFilePath(avatar.getFilePath());
        dto.setFileSize(avatar.getFileSize());
        dto.setMediaType(avatar.getMediaType());
        dto.setData(avatar.getData());
        dto.setStudent(StudentDTO.fromEntity(avatar.getStudent()));

        return dto;
    }

    public ru.hogwarts.school.model.Avatar toAvatar() {
        ru.hogwarts.school.model.Avatar avatar = new ru.hogwarts.school.model.Avatar();
        avatar.setId(this.id);
        avatar.setFilePath(this.filePath);
        avatar.setFileSize(this.fileSize);
        avatar.setMediaType(this.mediaType);
        avatar.setData(this.data);
        avatar.setStudent(this.student.toEntity());

        return avatar;
    }
}