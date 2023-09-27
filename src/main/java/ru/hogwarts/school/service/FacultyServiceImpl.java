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

    private final FacultyRepository facultyRepository;
    private final StudentService studentService;
    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));
    }

    @Override
    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyRepository.existsById(faculty.getId())) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public void removeFaculty(long id) {
        if (!facultyRepository.existsById(id)) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        facultyRepository.deleteById(id);
    }

    @Override
    public List<Faculty> getFacultyByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
    @Override
    public List<Student> getStudentsOfFaculty(Long id){
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));
        return faculty.getStudents();
    }

    @Override
    public Student changeStudentFaculty(Long studentId, Long facultyId) {
        Student student = studentService.findStudent(studentId)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));

        student.setFaculty(faculty);
        return studentService.updateStudent(student);
    }
}