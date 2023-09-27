package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
@RequiredArgsConstructor
@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;
    private final StudentService studentService;
    @Override
    public Faculty addFaculty(Faculty faculty) {
        return repository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        if (!repository.existsById(faculty.getId())) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        return repository.save(faculty);
    }

    @Override
    public void removeFaculty(long id) {
        if (!repository.existsById(id)) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        repository.deleteById(id);
    }

    @Override
    public List<Faculty> getFacultyByColor(String color) {
        return repository.findAllByColor(color);
    }

    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        return repository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
    @Override
    public Student addStudentToFaculty(Long facultyId, Student newStudent) {
        Faculty faculty = repository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));

        newStudent.setFaculty(faculty);

        Student savedStudent =  studentService.addStudent(newStudent);
        savedStudent.setFaculty(faculty);
        return studentService.updateStudent(savedStudent);
    }
    @Override
    public Student changeStudentFaculty(Long studentId, Long facultyId) {
        Student student = studentService.findStudent(studentId);
        Faculty faculty = repository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));

        student.setFaculty(faculty);

        return studentService.addStudent(student);
    }
}