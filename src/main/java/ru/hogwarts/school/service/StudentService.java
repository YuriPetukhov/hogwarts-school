package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student addStudent(Student student);

    Optional<Student> findStudent(Long id);

    Student updateStudent(Long id, Student student);

    void removeStudent(long id);

    List<Student> getStudentsByAge(int age);

    List<Student> findByAgeBetween(int min, int max);

    Faculty getFacultyOfStudent(Long id);

    Faculty selectRandomFaculty();
    Long countAllStudents();

    Double getAverageAge();

    List<Student> findLastFiveStudents();

    List<String> findAllStudentsByFirstLetter(Character firstLetter);

    Double getAverageAgeInStream();
}
