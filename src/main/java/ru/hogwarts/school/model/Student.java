package ru.hogwarts.school.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@Entity(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private int age;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    public Student() {
    }
    public Student(Long id, String name, int age, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
