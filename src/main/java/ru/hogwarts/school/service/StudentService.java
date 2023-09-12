package ru.hogwarts.school.service;

import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {
    Student addStudent(Student student);
    Student findStudent(Long id) throws ElementNotExistException;
    Student updateStudent(Student student) throws ElementNotExistException;
    void removeStudent(long id) throws ElementNotExistException;
    List<Student> getStudentsByAge(int age);
}
