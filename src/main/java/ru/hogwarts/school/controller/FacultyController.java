package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return service.addFaculty(faculty);
    }
    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) throws ElementNotExistException {
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
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long id) throws ElementNotExistException {
        try {
            service.removeFaculty(id);
            return ResponseEntity.ok().build();
        } catch (ElementNotExistException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Faculty>> getFacultyByColor(@RequestParam String color) {
        List<Faculty> faculties = service.getFacultyByColor(color);
        if (faculties.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(faculties);
        }
    }
}
