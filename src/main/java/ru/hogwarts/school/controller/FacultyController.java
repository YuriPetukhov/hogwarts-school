package ru.hogwarts.school.controller;

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

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;
    private final MapperService mapperService;

    public FacultyController(FacultyService facultyService, MapperService mapperService) {
        this.facultyService = facultyService;
        this.mapperService = mapperService;
    }

    @PostMapping
    public ResponseEntity<FacultyGeneralDTO> addFaculty(@RequestBody FacultyGeneralDTO facultyGeneralDTO) {
        Faculty faculty = facultyService.addFaculty(mapperService.toEntityFacultyGeneral(facultyGeneralDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toDtoFacultyGeneral(faculty));
    }

    @GetMapping("{id}")
    public ResponseEntity<FacultyDTO> findFaculty(@PathVariable Long id) {
        Faculty foundFaculty = facultyService.findFaculty(id);
        FacultyDTO ffoundFacultyDTO = mapperService.toDtoFaculty(foundFaculty);
            return ResponseEntity.ok(ffoundFacultyDTO);
    }

    @PutMapping
    public ResponseEntity<FacultyGeneralDTO> updateFaculty(@RequestBody FacultyGeneralDTO facultyGeneralDTO) {
        Faculty updatedFaculty = facultyService.updateFaculty(mapperService.toEntityFacultyGeneral(facultyGeneralDTO));
        return ResponseEntity.status(HttpStatus.OK).body(mapperService.toDtoFacultyGeneral(updatedFaculty));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
    }

    @GetMapping("/color")
    public ResponseEntity<List<FacultyGeneralDTO>> getFacultyByColor(@RequestParam String color) {
        List<Faculty> faculties = facultyService.getFacultyByColor(color);
        List<FacultyGeneralDTO> facultyDTOS = faculties.stream()
                .map(mapperService::toDtoFacultyGeneral)
                .collect(Collectors.toList());
        return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping
    public ResponseEntity<List<FacultyGeneralDTO>> findByNameIgnoreCaseOrColorIgnoreCase(
            @RequestParam String nameOrColor) {
        List<Faculty> faculties = facultyService.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
        List<FacultyGeneralDTO> facultyDTOS = faculties.stream()
                .map(mapperService::toDtoFacultyGeneral)
                .collect(Collectors.toList());
            return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping("{id}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = facultyService.getStudentsOfFaculty(id);
        List<StudentDTO> studentsDto = students.stream()
                .map(mapperService::toDtoStudent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(studentsDto);
    }
}
