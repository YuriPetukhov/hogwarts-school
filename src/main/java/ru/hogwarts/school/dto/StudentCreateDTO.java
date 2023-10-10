package ru.hogwarts.school.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Data
@Getter
@Setter
public class StudentCreateDTO {
    private Long id;
    private String name;
    private int age;

    public StudentCreateDTO(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public StudentCreateDTO() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentCreateDTO)) return false;
        StudentCreateDTO that = (StudentCreateDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
