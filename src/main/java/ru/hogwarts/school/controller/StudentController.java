package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            throw new ElementNotExistException("Студент не найден");
        } else {
            return ResponseEntity.ok(foundStudent);
        }
    }

    @PutMapping
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(student);
        if (updatedStudent == null) {
            throw new ElementNotExistException("Не получилось внести изменения");
        } else {
            return ResponseEntity.ok(updatedStudent);
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
    }

    @GetMapping("/age")
    public ResponseEntity<List<Student>> getStudentsByAge(
            @RequestParam(required = false) Integer age) {
        if (age == null) {
            throw new ElementNotExistException("Не указан параметр age");
        }
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping("/age-range")
    public ResponseEntity<List<Student>> findByAgeBetween(
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max) {
        if (min == null || max == null) {
            throw new ElementNotExistException("Не указаны параметры min и max");
        } else if (min >= max) {
            throw new ElementNotExistException("Неправильные параметры диапазона");
        }
        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
    }

    @GetMapping("{studentId}/faculty")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable("studentId") Long id) {
        Faculty faculty = studentService.getFacultyOfStudent(id);

        if (faculty == null) {
            throw new ElementNotExistException("Факультет не найден");
        } else {
            return ResponseEntity.ok(faculty);
        }
    }
}