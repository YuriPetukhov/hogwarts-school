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
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static ru.hogwarts.school.dto.FacultyDTO.mapFacultiesToDtoList;
import static ru.hogwarts.school.dto.StudentDTO.mapStudentsToDtoList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyDTO> addFaculty(@RequestBody FacultyDTO facultyDTO) {
        Faculty faculty = facultyService.addFaculty(facultyDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(FacultyDTO.fromEntity(faculty));
    }

    @GetMapping("{id}")
    public ResponseEntity<FacultyDTO> findFaculty(@PathVariable Long id) {
        Faculty foundFaculty = facultyService.findFaculty(id);
        FacultyDTO ffoundFacultyDTO = FacultyDTO.fromEntity(foundFaculty);
            return ResponseEntity.ok(ffoundFacultyDTO);
    }

    @PutMapping
    public ResponseEntity<FacultyDTO> updateFaculty(@RequestBody FacultyDTO facultyDTO) {
            Faculty updatedFaculty = facultyService.updateFaculty(facultyDTO.toEntity());
            return ResponseEntity.status(HttpStatus.OK).body(FacultyDTO.fromEntity(updatedFaculty));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeFaculty(@PathVariable Long id) {
        facultyService.removeFaculty(id);
    }

    @GetMapping("/color")
    public ResponseEntity<List<FacultyDTO>> getFacultyByColor(@RequestParam(required = false) String color) {
        if (color == null) {
            throw new ElementNotExistException("Не указан параметр color");
        }
        List<Faculty> faculties = facultyService.getFacultyByColor(color);
        List<FacultyDTO> facultyDTOS = mapFacultiesToDtoList(faculties);
        return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping
    public ResponseEntity<List<FacultyDTO>> findByNameIgnoreCaseOrColorIgnoreCase(
            @RequestParam String nameOrColor) {
        List<Faculty> faculties = facultyService.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
        List<FacultyDTO> facultyDTOS = mapFacultiesToDtoList(faculties);
            return ResponseEntity.ok(facultyDTOS);
    }

    @GetMapping("{id}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsOfFaculty(@PathVariable Long id) {
        List<Student> students = facultyService.getStudentsOfFaculty(id);
        List<StudentDTO> studentDTOS = mapStudentsToDtoList(students);
        return ResponseEntity.ok(studentDTOS);
    }

    @PutMapping(value = "/{facultyId}/students/{studentId}")
    public ResponseEntity<StudentDTO> changeStudentFaculty(
            @PathVariable("facultyId") Long facultyId,
            @PathVariable("studentId") Long studentId) {
        Student updatedStudent = facultyService.changeStudentFaculty(studentId, facultyId);
        StudentDTO updatedStudentDTO = StudentDTO.fromEntity(updatedStudent);
        return ResponseEntity.ok(updatedStudentDTO);
    }
}
