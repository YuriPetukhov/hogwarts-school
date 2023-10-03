package ru.hogwarts.school.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.FacultyGeneralDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static ru.hogwarts.school.dto.FacultyDTO.mapFacultiesToDtoList;
import static ru.hogwarts.school.dto.FacultyGeneralDTO.mapFacultiesGeneralToDtoList;
import static ru.hogwarts.school.dto.StudentDTO.mapStudentsToDtoList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyGeneralDTO> addFaculty(@RequestBody FacultyGeneralDTO facultyGeneralDTO) {
        Faculty faculty = facultyService.addFaculty(facultyGeneralDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(FacultyGeneralDTO.fromEntity(faculty));
    }

    @GetMapping("{id}")
    public ResponseEntity<FacultyDTO> findFaculty(@PathVariable Long id) {
        Faculty foundFaculty = facultyService.findFaculty(id);
        FacultyDTO ffoundFacultyDTO = FacultyDTO.fromEntity(foundFaculty, true);
            return ResponseEntity.ok(ffoundFacultyDTO);
    }

    @PutMapping
    public ResponseEntity<FacultyGeneralDTO> updateFaculty(@RequestBody FacultyGeneralDTO facultyGeneralDTO) {
        Faculty updatedFaculty = facultyService.updateFaculty(facultyGeneralDTO.toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(FacultyGeneralDTO.fromEntity(updatedFaculty));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
    }

    @GetMapping("/color")
    public ResponseEntity<List<FacultyGeneralDTO>> getFacultyByColor(@RequestParam String color) {
        List<Faculty> faculties = facultyService.getFacultyByColor(color);
        List<FacultyGeneralDTO> facultyDTOS = mapFacultiesGeneralToDtoList(faculties);
        return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping
    public ResponseEntity<List<FacultyGeneralDTO>> findByNameIgnoreCaseOrColorIgnoreCase(
            @RequestParam String nameOrColor) {
        List<Faculty> faculties = facultyService.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
        List<FacultyGeneralDTO> facultyDTOS = mapFacultiesGeneralToDtoList(faculties);
            return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping("{id}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = facultyService.getStudentsOfFaculty(id);
        List<StudentDTO> studentDTOS = mapStudentsToDtoList(students);
        return ResponseEntity.ok(studentDTOS);
    }
}
