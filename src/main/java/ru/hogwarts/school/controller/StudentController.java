package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("student")
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
    public Student findStudent(@PathVariable Long id){
        return service.findStudent(id);
    }
    @PutMapping
    public Student updateStudent(@RequestBody Student student){
        return service.updateStudent(student);
    }
    @DeleteMapping("{id}")
    public Student removeStudent(@PathVariable Long id){
        return service.removeStudent(id);
    }
    @GetMapping
    public List<Student> getStudentsByAge(@RequestParam int age){
        return service.getStudentsByAge(age);
    }
}
