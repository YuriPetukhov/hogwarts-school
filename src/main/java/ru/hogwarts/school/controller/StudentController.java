package ru.hogwarts.school.controller;

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
    public Student addStudent(@RequestBody Student student){
        return service.addStudent(student);
    }
    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) throws ElementNotExistException {
        Student foundStudent = service.findStudent(id);
        if (foundStudent == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundStudent);
        }
    }
    @PutMapping
    public ResponseEntity<Student> updateFaculty(@RequestBody Student student) {
        try {
            Student updatedStudent = service.updateStudent(student);
            return ResponseEntity.ok(updatedStudent);
        } catch (ElementNotExistException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Student> removeStudent(@PathVariable Long id) {
        try {
            service.removeStudent(id);
            return ResponseEntity.ok().build();
        } catch (ElementNotExistException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping
    public ResponseEntity<List<Student>> getStudentsByAge(@RequestParam int age){
        List<Student> students = service.getStudentsByAge(age);
        if(students.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }
}
