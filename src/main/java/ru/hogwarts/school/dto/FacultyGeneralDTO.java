package ru.hogwarts.school.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacultyGeneralDTO {
    private Long id;
    private String name;
    private String color;
}
