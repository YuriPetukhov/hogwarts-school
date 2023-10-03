package ru.hogwarts.school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hogwarts.school.model.Student;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateDTO {
    private Long id;
    private String name;
    private int age;

    public Student toEntity() {
        Student student = new Student();
        student.setId(this.getId());
        student.setName(this.getName());
        student.setAge(this.getAge());
        return student;
    }

}
