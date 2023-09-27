package ru.hogwarts.school.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого студента нет в базе"));
    }

    @Override
    public Student updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new ElementNotExistException("Такого студента нет в базе");
        }
        return studentRepository.save(student);
    }

    @Override
    public void removeStudent(long id) {
        if (!studentRepository.existsById(id)) {
            throw new ElementNotExistException("Такого студента нет в базе");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        return studentRepository.findStudentByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyOfStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ElementNotExistException("Такого студента нет в базе"));
        return student.getFaculty();
    }
}