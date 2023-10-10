package ru.hogwarts.school.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StudentDTO {
    private Long id;
    private String name;
    private int age;
    private Long facultyId;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String name, int age, Long facultyId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.facultyId = facultyId;
    }
}
