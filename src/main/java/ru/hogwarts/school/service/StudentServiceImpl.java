package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        return repository.save(student);
    }

    public Student findStudent(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого студента нет в базе"));
    }

    public Student updateStudent(Student student) {
        if (!repository.existsById(student.getId())) {
            throw new ElementNotExistException("Такого студента нет в базе");
        }
        return repository.save(student);
    }

    public void removeStudent(long id) {
        if (!repository.existsById(id)) {
            throw new ElementNotExistException("Такого студента нет в базе");
        }
        repository.deleteById(id);
    }

    public List<Student> getStudentsByAge(int age) {
        return repository.findStudentByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return repository.findByAgeBetween(min, max);
    }
    public List<Student> getStudentsByFaculty(Long facultyId) {
        return repository.findByFacultyId(facultyId);
    }
}