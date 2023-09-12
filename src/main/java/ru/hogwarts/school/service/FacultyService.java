package ru.hogwarts.school.service;

import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id) throws ElementNotExistException;

    Faculty updateFaculty(Faculty faculty) throws ElementNotExistException;

   void removeFaculty(long id) throws ElementNotExistException;

   List<Faculty> getFacultyByColor(String color);
}
