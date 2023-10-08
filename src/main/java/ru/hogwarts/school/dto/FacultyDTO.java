package ru.hogwarts.school.dto;

import lombok.*;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FacultyDTO {
    private Long id;
    private String name;
    private String color;
    private List<StudentDTO> students;
}