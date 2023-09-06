package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAndReturnStudentWithId() {
        Student student = new Student(null, "Harry Potter", 11);
        Student savedStudent = studentService.addStudent(student);

        assertNotNull(savedStudent.getId());
        assertEquals(student.getName(), savedStudent.getName());
        assertEquals(student.getAge(), savedStudent.getAge());
    }

    @Test
    void shouldFindAndReturnStudentById() {
        Student student = new Student(null, "Hermione Granger", 11);
        Student savedStudent = studentService.addStudent(student);
        Student foundStudent = studentService.findStudent(savedStudent.getId());

        assertNotNull(foundStudent);
        assertEquals(savedStudent, foundStudent);
    }

    @Test
    void shouldUpdateAndReturnModifiedStudent() {
        Student student = new Student(null, "Ron Weasley", 11);
        Student savedStudent = studentService.addStudent(student);
        savedStudent.setName("Ronald Weasley");
        savedStudent.setAge(12);

        Student updatedStudent = studentService.updateStudent(savedStudent);

        assertNotNull(updatedStudent);
        assertEquals(savedStudent, updatedStudent);
    }

    @Test
    void shouldDeleteStudentById() {
        Student student = new Student(null, "Neville Longbottom", 11);
        Student savedStudent = studentService.addStudent(student);
        studentService.removeStudent(savedStudent.getId());

        assertNull(studentService.findStudent(savedStudent.getId()));
    }

    @Test
    void shouldReturnAllStudentsWithGivenAge() {
        studentService.addStudent(new Student(null, "Harry Potter", 11));
        studentService.addStudent(new Student(null, "Ron Weasley", 11));
        studentService.addStudent(new Student(null, "Hermione Granger", 11));
        studentService.addStudent(new Student(null, "Neville Longbottom", 12));

        List<Student> studentsWithAge11 = studentService.getStudentsByAge(11);

        assertEquals(3, studentsWithAge11.size());
        assertTrue(studentsWithAge11.stream().allMatch(student -> student.getAge() == 11));
    }
}
