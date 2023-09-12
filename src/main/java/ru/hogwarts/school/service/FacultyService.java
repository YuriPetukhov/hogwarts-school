package ru.hogwarts.school.service;

import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id) throws ElementNotExistException;

    Faculty updateFaculty(Faculty faculty) throws ElementNotExistException;

   void removeFaculty(long id) throws ElementNotExistException;

   List<Faculty> getFacultyByColor(String color);
}
