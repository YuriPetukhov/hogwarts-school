package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.MapperService;
import ru.hogwarts.school.dto.StudentCreateDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final MapperService mapperService;

    public StudentController(StudentService studentService, MapperService mapperService) {
        this.studentService = studentService;
        this.mapperService = mapperService;
    }

    @PostMapping
    @Operation(summary = "Добавить студента")
    public ResponseEntity<StudentDTO> addStudent(@RequestBody StudentCreateDTO studentCreateDTO) {
        Student student = studentService.addStudent(mapperService.toEntityStudentCreate(studentCreateDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toDtoStudent(student));
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentDTO> findStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id)
                .orElseThrow(() ->
                        new ElementNotExistException("Такого студента нет в базе"));
        StudentDTO studentDTO = mapperService.toDtoStudent(student);
        return ResponseEntity.ok(studentDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentDTO studentDTO) {
        Student student = studentService.updateStudent(id, mapperService.toEntityStudent(studentDTO));
        return ResponseEntity.status(HttpStatus.OK).body(mapperService.toDtoStudent(student));
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
        List<StudentDTO> studentsDto = students.stream()
                .map(mapperService::toDtoStudent)
                .collect(Collectors.toList());
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
        List<StudentDTO> studentsDto = students.stream()
                .map(mapperService::toDtoStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsDto);
    }

    @GetMapping("{studentId}/faculty")
    public ResponseEntity<FacultyDTO> getFacultyOfStudent(@PathVariable("studentId") Long id) {
        Faculty faculty = studentService.getFacultyOfStudent(id);
        if (faculty == null) {
            throw new ElementNotExistException("У студента нет факультета");
        } else {
            return ResponseEntity.ok(mapperService.toDtoFaculty(faculty));
        }
    }

    @GetMapping("count-all-students")
    public Long countAllStudents() {
        return studentService.countAllStudents();
    }

    @GetMapping("average-age")
    public Double getAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("last-five-students")
    public ResponseEntity<List<StudentDTO>> findLastFiveStudents() {
        List<Student> students = studentService.findLastFiveStudents();
        List<StudentDTO> studentsDto = students.stream()
                .map(mapperService::toDtoStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsDto);
    }
}