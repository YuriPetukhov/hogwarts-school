package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static ru.hogwarts.school.dto.StudentDTO.mapStudentsToDtoList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentDTO studentDTO) {
        Student student = studentService.addStudent(studentDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(StudentDTO.fromEntity(student));
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentDTO> findStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id)
                .orElseThrow(() ->
                        new ElementNotExistException("Такого студента нет в базе"));
        StudentDTO studentDTO = StudentDTO.fromEntity(student);
        return ResponseEntity.ok(studentDTO);
    }

    @PutMapping
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO) {
        Student student = studentService.updateStudent(studentDTO.toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(StudentDTO.fromEntity(student));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeStudent(@PathVariable Long id) {
        studentService.removeStudent(id);
    }

    @GetMapping("/age")
    public ResponseEntity<List<StudentDTO>> getStudentsByAge(
            @RequestParam(required = false) Integer age) {
        if (age == null) {
            throw new ElementNotExistException("Не указан параметр age");
        }
        List<Student> students = studentService.getStudentsByAge(age);
        List<StudentDTO> studentsDto = mapStudentsToDtoList(students);
        return ResponseEntity.ok(studentsDto);
    }

    @GetMapping("/age-range")
    public ResponseEntity<List<StudentDTO>> findByAgeBetween(
            @RequestParam(required = false) Integer min,
            @RequestParam(required = false) Integer max) {
        if (min == null || max == null) {
            throw new ElementNotExistException("Не указаны параметры min и max");
        } else if (min >= max) {
            throw new ElementNotExistException("Неправильные параметры диапазона");
        }
        List<Student> students = studentService.findByAgeBetween(min, max);
        List<StudentDTO> studentsDto = mapStudentsToDtoList(students);
        return ResponseEntity.ok(studentsDto);
    }

    @GetMapping("{studentId}/faculty")
    public ResponseEntity<FacultyDTO> getFacultyOfStudent(@PathVariable("studentId") Long id) {
        Faculty faculty = studentService.getFacultyOfStudent(id);
        if (faculty == null) {
            throw new ElementNotExistException("У студента нет факультета");
        } else {
            return ResponseEntity.ok(FacultyDTO.fromEntity(faculty));
        }
    }
}