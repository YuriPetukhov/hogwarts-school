package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    Faculty updateFaculty(Faculty faculty);

   void removeFaculty(long id);

   List<Faculty> getFacultyByColor(String color);
   List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

}
