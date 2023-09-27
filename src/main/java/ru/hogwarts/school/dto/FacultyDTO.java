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
public class FacultyDTO {
    private Long id;
    private String name;
    private String color;
    private List<StudentDTO> students;

    public FacultyDTO(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }


    public static FacultyDTO fromEntity(Faculty faculty) {
        if (faculty == null) {
            return null;
        }

        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setId(faculty.getId());
        facultyDTO.setName(faculty.getName());
        facultyDTO.setColor(faculty.getColor());

        List<StudentDTO> studentDTOS = faculty.getStudents().stream()
                .map(StudentDTO::fromEntity)
                .collect(Collectors.toList());
        facultyDTO.setStudents(studentDTOS);

        return facultyDTO;
    }

    public Faculty toEntity() {
        Faculty faculty = new Faculty();
        faculty.setId(this.getId());
        faculty.setName(this.getName());
        faculty.setColor(this.getColor());

        return faculty;
    }
    public static List<FacultyDTO> mapFacultiesToDtoList(List<Faculty> faculties) {
        return faculties.stream()
                .map(faculty -> new FacultyDTO(faculty.getId(), faculty.getName(), faculty.getColor()))
                .collect(Collectors.toList());
    }
}