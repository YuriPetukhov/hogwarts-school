package ru.hogwarts.school.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@Entity (name = "avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] data;

    @OneToOne
    private Student student;

    public Avatar() {
    }

    public Avatar(Long id, String filePath, long fileSize, String mediaType, byte[] data, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avatar)) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(id, avatar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
