package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);
    Student findStudent(Long id);
    Student updateStudent(Student student);
    void removeStudent(long id);
    List<Student> getStudentsByAge(int age);
    List<Student>findByAgeBetween(int min, int max);

    List<Student> getStudentsByFaculty(Long facultyId);

    Student getFacultyOfStudent(Long id);
}
