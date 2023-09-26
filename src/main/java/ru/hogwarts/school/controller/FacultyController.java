package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<String> removeFaculty(@PathVariable Long id) {
        try {
            service.removeFaculty(id);
            return ResponseEntity.ok("The faculty has been successfully removed.");
        } catch (ElementNotExistException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/color")
    public List<Faculty> getFacultyByColor(@RequestParam String color) {
        return service.getFacultyByColor(color);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> findByNameIgnoreCaseOrColorIgnoreCase(
            @RequestParam String nameOrColor) {
        List<Faculty> faculties = service.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);

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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(faculty.getStudents());
        }
    }
}
