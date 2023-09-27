package ru.hogwarts.school.dto;

import lombok.*;
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

    public StudentDTO(Student student) {
        if (student != null) {
            this.id = student.getId();
            this.name = student.getName();
            this.age = student.getAge();
        }
    }

    public static StudentDTO fromEntity(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setAge(student.getAge());
        return studentDTO;
    }

    public Student toEntity() {
        Student student = new Student();
        student.setId(this.getId());
        student.setName(this.getName());
        student.setAge(this.getAge());
        return student;
    }
    public static List<StudentDTO> mapStudentsToDtoList(List<Student> students) {
        return students.stream()
                .map(student -> new StudentDTO(student.getId(), student.getName(), student.getAge()))
                .collect(Collectors.toList());
    }
}
