package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    Faculty updateFaculty(Faculty faculty);

    void removeFaculty(long id);

    List<Faculty> getFacultyByColor(String color);

    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
    List<Student> getStudentsOfFaculty(Long id);

    List<Faculty> findAll();

    Optional<Faculty> findById(Long id);
}
