package ru.hogwarts.school.dto;


import java.util.List;

public class FacultyDTO {
    private Long id;
    private String name;
    private String color;
    private List<StudentDTO> students;

    public FacultyDTO() {
    }

    public FacultyDTO(Long id, String name, String color, List<StudentDTO> students) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDTO> students) {
        this.students = students;
    }
}