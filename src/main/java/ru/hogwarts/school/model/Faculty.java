package ru.hogwarts.school.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "faculty")
    @ToString.Exclude
    private List<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Faculty faculty)) return false;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

}
