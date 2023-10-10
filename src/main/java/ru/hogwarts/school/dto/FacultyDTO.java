package ru.hogwarts.school.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
@Setter
public class FacultyDTO {
    private Long id;
    private String name;
    private String color;
    private List<StudentDTO> students;

    public FacultyDTO() {
    }

    public FacultyDTO(Long id, String name, String color, List<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.students = students;
    }
}