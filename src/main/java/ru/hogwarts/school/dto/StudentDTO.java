package ru.hogwarts.school.dto;

import lombok.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private Long id;
    private String name;
    private int age;
    private Long facultyId;

    public StudentDTO(Student student) {
        if (student != null) {
            this.id = student.getId();
            this.name = student.getName();
            this.age = student.getAge();
        }
    }
    public StudentDTO(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public static StudentDTO fromEntity(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setAge(student.getAge());

        if (student.getFaculty() != null) {
            studentDTO.setFacultyId(student.getFaculty().getId());
        }

        return studentDTO;
    }

    public Student toEntity() {
        Student student = new Student();
        student.setId(this.getId());
        student.setName(this.getName());
        student.setAge(this.getAge());

        if (this instanceof StudentDTO && this.getFacultyId() != null) {
            Faculty faculty = new Faculty();
            faculty.setId(this.getFacultyId());
            student.setFaculty(faculty);
        }

        return student;
    }
    public static List<StudentDTO> mapStudentsToDtoList(List<Student> students) {
        return students.stream()
                .map(student -> new StudentDTO(student.getId(), student.getName(), student.getAge()))
                .collect(Collectors.toList());
    }
}
