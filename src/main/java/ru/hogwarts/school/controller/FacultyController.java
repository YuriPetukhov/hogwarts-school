package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.FacultyGeneralDTO;
import ru.hogwarts.school.dto.MapperService;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;
    private final MapperService mapperService;

    @PostMapping
    @Operation(summary = "Открыть новый факультет")
    public ResponseEntity<FacultyGeneralDTO> addFaculty(@RequestBody FacultyGeneralDTO facultyGeneralDTO) {
        Faculty faculty = facultyService.addFaculty(mapperService.toEntityFacultyGeneral(facultyGeneralDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toDtoFacultyGeneral(faculty));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Найти факультет")
    public ResponseEntity<FacultyDTO> findFaculty(@PathVariable Long id) {
        Faculty foundFaculty = facultyService.findFaculty(id);
        FacultyDTO foundFacultyDTO = mapperService.toDtoFaculty(foundFaculty);
            return ResponseEntity.ok(foundFacultyDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить параметры факультета")
    public ResponseEntity<FacultyGeneralDTO> updateFaculty(
            @PathVariable Long id,
            @RequestBody FacultyGeneralDTO facultyGeneralDTO) {
        Faculty updatedFaculty = facultyService.updateFaculty(id, mapperService.toEntityFacultyGeneral(facultyGeneralDTO));
        return ResponseEntity.status(HttpStatus.OK).body(mapperService.toDtoFacultyGeneral(updatedFaculty));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Закрыть факультет")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
    }

    @GetMapping("/color")
    @Operation(summary = "Найти факультеты определенного цвета")
    public ResponseEntity<List<FacultyGeneralDTO>> getFacultyByColor(@RequestParam String color) {
        List<Faculty> faculties = facultyService.getFacultyByColor(color);
        List<FacultyGeneralDTO> facultyDTOS = faculties.stream()
                .map(mapperService::toDtoFacultyGeneral)
                .collect(Collectors.toList());
        return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping
    @Operation(summary = "Найти факультет по названию или цвету без учета регистра написания")
    public ResponseEntity<List<FacultyDTO>> findByNameIgnoreCaseOrColorIgnoreCase(
            @RequestParam String nameOrColor) {
        List<Faculty> faculties = facultyService.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
        List<FacultyDTO> facultyDTOS = faculties.stream()
                .map(mapperService::toDtoFaculty)
                .collect(Collectors.toList());
            return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping("{id}/students")
    @Operation(summary = "Получить список студентов факультета")
    public ResponseEntity<List<StudentDTO>> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = facultyService.getStudentsOfFaculty(id);
        List<StudentDTO> studentsDto = students.stream()
                .map(mapperService::toDtoStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsDto);
    }
}
