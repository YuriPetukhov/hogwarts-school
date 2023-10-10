package ru.hogwarts.school.dto;

import lombok.*;

import java.util.Objects;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateDTO {
    private Long id;
    private String name;
    private int age;

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
