package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentControllerRestTemplateTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static List<Student> students;
    private static Long studentId;

    @BeforeAll
    public static void setup() {

        students = new ArrayList<>();
        Student student1 = new Student();
        student1.setName("John");
        student1.setAge(10);
        students.add(student1);

        Student student2 = new Student();
        student2.setName("Jane");
        student2.setAge(15);
        students.add(student2);

        Student student3 = new Student();
        student3.setName("Bob");
        student3.setAge(20);
        students.add(student3);
    }

    @AfterEach
    public void cleanup() {
        for (Student student : students) {
            studentRepository.delete(student);
        }
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    @Order(1)
    public void testAddStudent() throws Exception {
        Student testStudent = new Student();
        testStudent.setAge(12);
        testStudent.setName("Peter");

        Student savedTestStudent = this.testRestTemplate.postForObject("http://localhost:" + localServerPort +
                "/student", testStudent, Student.class);
        Assertions.assertThat(savedTestStudent).isNotNull();
        studentId = savedTestStudent.getId();
    }

    @Test
    @Order(2)
    void testFindStudent() {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + localServerPort +
                        "/student/1", String.class))
                .isNotNull();
    }

    @Test
    @Order(3)
    void testUpdateStudent() {
        if (studentId == null) {
            throw new IllegalStateException("Такого студента нет в базе.");
        }

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("John");
        updatedStudent.setAge(15);

        ResponseEntity<Student> response = this.testRestTemplate.exchange("http://localhost:" + localServerPort +
                "/student", HttpMethod.PUT, new HttpEntity<>(updatedStudent), Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("John");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(15);
    }

    @Test
    @Order(4)
    void testRemoveStudent() {
        if (studentId == null) {
            throw new IllegalStateException("Такого студента нет в базе.");
        }

        this.testRestTemplate.delete("http://localhost:" + localServerPort + "/student/" + studentId);

        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        Assertions.assertThat(deletedStudent).isEmpty();
    }

    @Test
    @Order(5)
    void testGetStudentsByAge() {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + localServerPort +
                        "/student/12", String.class))
                .isNotNull();
    }

    @Test
    @Order(6)
    void testFindByAgeBetween() {
        for (Student student : students) {
            studentRepository.save(student);
        }

        String url = "http://localhost:" + localServerPort + "/student/age-range?min=10&max=20";

        ResponseEntity<List> response = testRestTemplate.getForEntity(url, List.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @Order(7)
    void testGetFacultyOfStudent() {
        Student testStudent = new Student();
        Faculty testFaculty = new Faculty();
        testFaculty.setName("Gryffindor");
        testFaculty.setColor("green");
        facultyRepository.save(testFaculty);
        testStudent.setName("Tom");
        testStudent.setAge(22);
        testStudent.setFaculty(testFaculty);
        Student savedStudent = studentRepository.save(testStudent);

        String url = "http://localhost:" + localServerPort + "/student/" + savedStudent.getId() + "/faculty";

        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(url, Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo(testFaculty.getName());

        studentRepository.delete(savedStudent);
    }

}