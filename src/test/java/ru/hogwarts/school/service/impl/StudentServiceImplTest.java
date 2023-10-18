package ru.hogwarts.school.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testPrintNamesToConsole_noStudents() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        try {
            studentService.printNamesToConsole();
        } catch (ElementNotExistException e) {
            assertEquals("Нет студентов в базе", e.getMessage());
        }
    }

    @Test
    public void testPrintNamesToConsole_lessThanSixStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Harry", 21, null),
                new Student(2L, "Ron", 21, null),
                new Student(3L, "Hermione", 21, null)
        );

        when(studentRepository.findAll()).thenReturn(students);
        studentService.printNamesToConsole();

        assertEquals(3, students.size());

        students.forEach(student -> assertTrue(outContent.toString().contains("Имя студента: " + student.getName())));

    }

    @Test
    public void testPrintNamesToConsole_multipleOfSixStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Harry", 21, null),
                new Student(2L, "Ron", 21, null),
                new Student(3L, "Hermione", 21, null),
                new Student(4L, "Luna", 21, null),
                new Student(5L, "Neville", 21, null),
                new Student(6L, "Ginny", 21, null)
        );
        when(studentRepository.findAll()).thenReturn(students);
        studentService.printNamesToConsole();

        assertEquals(6, students.size());

        students.forEach(student -> assertTrue(outContent.toString().contains("Имя студента: " + student.getName())));
    }


    @Test
    public void testPrintNamesToConsole_notMultipleOfSixStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Harry", 21, null),
                new Student(2L, "Ron", 21, null),
                new Student(3L, "Hermione", 21, null),
                new Student(4L, "Luna", 21, null),
                new Student(5L, "Neville", 21, null),
                new Student(6L, "Ginny", 21, null),
                new Student(7L, "Dumbledore", 21, null)
        );
        when(studentRepository.findAll()).thenReturn(students);
        studentService.printNamesToConsole();

        assertEquals(7, students.size());

        students.forEach(student -> assertTrue(outContent.toString().contains("Имя студента: " + student.getName())));
    }

    @Test
    public void testPrintNamesToConsoleSynchronized_noStudents() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        try {
            studentService.printNamesToConsoleSynchronized();
        } catch (ElementNotExistException e) {
            assertEquals("Нет студентов в базе", e.getMessage());
        }
    }

    @Test
    public void testPrintNamesToConsoleSynchronized_lessThanSixStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Harry", 21, null),
                new Student(2L, "Ron", 21, null),
                new Student(3L, "Hermione", 21, null)
        );

        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn((long) students.size());

        studentService.printNamesToConsoleSynchronized();

        assertEquals(3, students.size());

        students.forEach(student ->
                assertTrue(outContent.toString().contains(student.getId().toString() + " Имя студента: " + student.getName())));
    }

    @Test
    public void testPrintNamesToConsoleSynchronized_multipleOfSixStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Harry", 21, null),
                new Student(2L, "Ron", 21, null),
                new Student(3L, "Hermione", 21, null),
                new Student(4L, "Luna", 21, null),
                new Student(5L, "Neville", 21, null),
                new Student(6L, "Ginny", 21, null)
        );
        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn((long) students.size());

        studentService.printNamesToConsoleSynchronized();

        assertEquals(6, students.size());

        students.forEach(student ->
                assertTrue(outContent.toString().contains(student.getId().toString() + " Имя студента: " + student.getName())));
    }


    @Test
    public void testPrintNamesToConsoleSynchronized_notMultipleOfSixStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Harry", 21, null),
                new Student(2L, "Ron", 21, null),
                new Student(3L, "Hermione", 21, null),
                new Student(4L, "Luna", 21, null),
                new Student(5L, "Neville", 21, null),
                new Student(6L, "Ginny", 21, null),
                new Student(7L, "Dumbledore", 21, null)
        );
        when(studentRepository.findAll()).thenReturn(students);
        when(studentRepository.countAllStudents()).thenReturn((long) students.size());

        studentService.printNamesToConsoleSynchronized();

        assertEquals(7, students.size());

        students.forEach(student ->
                assertTrue(outContent.toString().contains(student.getId().toString() + " Имя студента: " + student.getName())));
    }
}
