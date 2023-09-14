package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return service.addFaculty(faculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) {
        Faculty foundFaculty = service.findFaculty(id);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundFaculty);
        }
    }

    @PutMapping
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        try {
            Faculty updatedFaculty = service.updateFaculty(faculty);
            return ResponseEntity.ok(updatedFaculty);
        } catch (ElementNotExistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFaculty(@PathVariable Long id) {
        service.removeFaculty(id);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> findByNameOrColorIgnoreCase(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color) {
        List<Faculty> faculties;
        if (name != null && color != null) {
            faculties = service.findByNameOrColorIgnoreCase(name, color);
        } else if (name != null) {
            faculties = service.findByNameIgnoreCase(name);
        } else if (color != null) {
            faculties = service.getFacultyByColor(color);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculties);
        }
    }
    @GetMapping("{id}/students")
    public ResponseEntity<List<Student>> getStudentsOfFaculty(@PathVariable Long id) {
        Faculty faculty = service.findFaculty(id);

        if (faculty == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(faculty.getStudents());
        }
    }
}
