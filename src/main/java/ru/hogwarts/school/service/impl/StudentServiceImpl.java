package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotAcceptableException;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final FacultyService facultyService;


    @Override
    public Student addStudent(Student student) {
        if (!isValidStudent(student)) {
            throw new ElementNotAcceptableException("Недопустимые данные");
        }
        student.setFaculty(selectRandomFaculty());
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findStudent(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student updateStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new ElementNotExistException("Такого студента нет в базе");
        }

        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty existingFaculty = facultyService.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new ElementNotExistException("Факультет не найден"));
            student.setFaculty(existingFaculty);
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

    @Override
    public Faculty selectRandomFaculty() {
        List<Faculty> faculties = facultyService.findAll();
        if (faculties.isEmpty()) {
            throw new ElementNotExistException("Нет доступных факультетов");
        }
        int randomIndex = new Random().nextInt(faculties.size());
        return faculties.get(randomIndex);
    }

    @Override
    public Long countAllStudents() {
        return studentRepository.countAllStudents();
    }

    @Override
    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> findLastFiveStudents() {
        PageRequest of = PageRequest.of(0, 5);
        return studentRepository.findLastFiveStudents(of);
    }

    private boolean isValidStudent(Student student) {
        return student.getName() != null && student.getAge() >= 20;
    }
}