package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }
    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty){
        return service.addFaculty(faculty);
    }
    @GetMapping("{id}")
    public Faculty findFaculty(@PathVariable Long id){
        return service.findFaculty(id);
    }
    @PutMapping
    public Faculty updateFaculty(@RequestBody Faculty faculty){
        return service.updateFaculty(faculty);
    }
    @DeleteMapping("{id}")
    public Faculty removeFaculty(@PathVariable Long id){
        return service.removeFaculty(id);
    }
    @GetMapping
    public List<Faculty> getFacultyByColor(@RequestParam String color){
        return service.getFacultyByColor(color);
    }
}
