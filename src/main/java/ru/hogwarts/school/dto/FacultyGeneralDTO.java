package ru.hogwarts.school.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FacultyGeneralDTO {
    private Long id;
    private String name;
    private String color;

    public FacultyGeneralDTO() {
    }

    public FacultyGeneralDTO(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
