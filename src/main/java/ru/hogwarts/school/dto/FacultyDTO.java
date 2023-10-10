package ru.hogwarts.school.dto;


import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacultyDTO {
    private Long id;
    private String name;
    private String color;
    private List<StudentDTO> students;
}