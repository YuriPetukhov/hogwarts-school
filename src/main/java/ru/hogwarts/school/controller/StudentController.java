package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Student> findStudent(@PathVariable Long id){
        Student foundStudent = service.findStudent(id);
        if (foundStudent == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundStudent);
        }
    }
    @PutMapping
    public Student updateStudent(@RequestBody Student student){
        return service.updateStudent(student);
    }
    @DeleteMapping("{id}")
    public ResponseEntity removeStudent(@PathVariable Long id){
        service.removeStudent(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public List<Student> getStudentsByAge(@RequestParam int age){
        return service.getStudentsByAge(age);
    }
}
