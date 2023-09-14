package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return service.addStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        Student foundStudent = service.findStudent(id);
        if (foundStudent == null) {
            throw new ElementNotExistException("Студент не найден");
        } else {
            return ResponseEntity.ok(foundStudent);
        }
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = service.updateStudent(student);
        if (updatedStudent == null) {
            throw new ElementNotExistException("Не получилось внести изменения");
        } else {
            return ResponseEntity.ok(updatedStudent);
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeStudent(@PathVariable Long id) {
        service.removeStudent(id);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudentsByAgeOrByAgeBetween(
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max) {
        if (age == null && min != null && min.equals(max)) {
            return ResponseEntity.ok(service.getStudentsByAge(min));
        } else if (age != null && min == null && max == null) {
            return ResponseEntity.ok(service.getStudentsByAge(age));
        } else if (age == null && min != null && max != null && min < max) {
            return ResponseEntity.ok(service.findByAgeBetween(min, max));
        } else {
            throw new ElementNotExistException("Неправильные параметры");
        }
    }

    @GetMapping("/faculty/{facultyId}")
    public List<Student> getStudentsByFaculty(@PathVariable Long facultyId) {
        return service.getStudentsByFaculty(facultyId);
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long id) {
        Student student = service.getFacultyOfStudent(id);

        if (student == null) {
            throw new ElementNotExistException("Студент не найден");
        }

        if (student.getFaculty() == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(student.getFaculty());
        }
    }
}