package ru.hogwarts.school.dto;

import lombok.*;
import ru.hogwarts.school.model.Faculty;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FacultyGeneralDTO {
    private Long id;
    private String name;
    private String color;

    public static FacultyGeneralDTO fromEntity(Faculty faculty) {
        if (faculty == null) {
            return null;
        }

        FacultyGeneralDTO facultyDTO = new FacultyGeneralDTO();
        facultyDTO.setId(faculty.getId());
        facultyDTO.setName(faculty.getName());
        facultyDTO.setColor(faculty.getColor());

        return facultyDTO;
    }
    public Faculty toEntity() {
        Faculty faculty = new Faculty();
        faculty.setId(this.getId());
        faculty.setName(this.getName());
        faculty.setColor(this.getColor());

        return faculty;
    }
    public static List<FacultyGeneralDTO> mapFacultiesGeneralToDtoList(List<Faculty> faculties) {
        return faculties.stream()
                .map(faculty -> new FacultyGeneralDTO(faculty.getId(), faculty.getName(), faculty.getColor()))
                .collect(Collectors.toList());
    }
}
