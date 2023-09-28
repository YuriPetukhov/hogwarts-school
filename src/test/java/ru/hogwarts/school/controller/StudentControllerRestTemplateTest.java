package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.exception.ElementNotExistException;
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
    private static List<Faculty> faculties;
    private static Long studentId;

    @BeforeEach
    public void setup() {

        faculties = new ArrayList<>();
        Faculty faculty1 = new Faculty();
        faculty1.setId(1L);
        faculty1.setName("Faculty1");
        faculty1.setColor("Color1");
        faculties.add(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setId(2L);
        faculty2.setName("Faculty2");
        faculty2.setColor("Color2");
        faculties.add(faculty2);

        Faculty faculty3 = new Faculty();
        faculty3.setId(3L);
        faculty3.setName("Faculty3");
        faculty3.setColor("Color3");
        faculties.add(faculty3);

        students = new ArrayList<>();
        Student student1 = new Student();
        student1.setName("John");
        student1.setAge(30);
        students.add(student1);

        Student student2 = new Student();
        student2.setName("Jane");
        student2.setAge(25);
        students.add(student2);

        Student student3 = new Student();
        student3.setName("Bob");
        student3.setAge(22);
        students.add(student3);

    }

    @AfterEach
    public void cleanup() {
        for (Student student : students) {
            studentRepository.delete(student);
        }
        for (Faculty faculty : faculties) {
            facultyRepository.delete(faculty);
        }
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    @Order(1)
    public void testAddStudent() throws Exception {
        facultyRepository.save(faculties.get(0));
        Student testStudent = students.get(0);

        Student savedTestStudent = this.testRestTemplate.postForObject("http://localhost:" + localServerPort +
                "/student", testStudent, Student.class);

        Assertions.assertThat(savedTestStudent).isNotNull();
        Assertions.assertThat(savedTestStudent.getName()).isEqualTo(testStudent.getName());
        Assertions.assertThat(savedTestStudent.getAge()).isEqualTo(testStudent.getAge());

        studentId = savedTestStudent.getId();
    }

    @Test
    @Order(2)
    void testFindStudent() {

        Student testStudent = students.get(0);
        Student savedTestStudent = studentRepository.save(testStudent);

        Student foundStudent = this.testRestTemplate.getForObject("http://localhost:" + localServerPort +
                "/student/" + savedTestStudent.getId(), Student.class);

        Assertions.assertThat(foundStudent).isNotNull();
        Assertions.assertThat(foundStudent.getId()).isEqualTo(savedTestStudent.getId());
        Assertions.assertThat(foundStudent.getName()).isEqualTo(testStudent.getName());
        Assertions.assertThat(foundStudent.getAge()).isEqualTo(testStudent.getAge());

        studentRepository.delete(savedTestStudent);
    }

    @Test
    @Order(3)
    void testUpdateStudent() {

        Faculty savedFaculty = facultyRepository.save(faculties.get(0));
        Student student = students.get(0);
        Student savedStudent = studentRepository.save(student);


        savedStudent.setName("Mark");
        savedStudent.setAge(25);


        ResponseEntity<Student> response = this.testRestTemplate.exchange("http://localhost:" + localServerPort +
                "/student", HttpMethod.PUT, new HttpEntity<>(savedStudent), Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Mark");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(25);
    }

    @Test
    @Order(4)
    void testRemoveStudent() {
        if (studentId == null) {
            throw new ElementNotExistException("Такого студента нет в базе.");
        }

        this.testRestTemplate.delete("http://localhost:" + localServerPort + "/student/" + studentId);

        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        Assertions.assertThat(deletedStudent).isEmpty();
    }

    @Test
    @Order(5)
    void testGetStudentsByAge() {
        Faculty faculty = facultyRepository.save(faculties.get(0));
        Student student1 = studentRepository.save(students.get(0));
        Student student2 = studentRepository.save(students.get(1));
        Student student3 = studentRepository.save(students.get(2));

        ResponseEntity<List<Student>> studentsAge30 = testRestTemplate.exchange("http://localhost:" + localServerPort +
                "/student/age?age=30", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });
        ResponseEntity<List<Student>> studentsAge25 = testRestTemplate.exchange("http://localhost:" + localServerPort +
                "/student/age?age=25", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });
        ResponseEntity<List<Student>> studentsAge22 = testRestTemplate.exchange("http://localhost:" + localServerPort +
                "/student/age?age=22", HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });

        Assertions.assertThat(studentsAge30.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(studentsAge30.getBody()).size()).isEqualTo(1);
        Assertions.assertThat(Objects.requireNonNull(studentsAge30.getBody()).get(0).getName()).isEqualTo(student1.getName());

        Assertions.assertThat(studentsAge25.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(studentsAge25.getBody()).size()).isEqualTo(1);
        Assertions.assertThat(Objects.requireNonNull(studentsAge25.getBody()).get(0).getName()).isEqualTo(student2.getName());

        Assertions.assertThat(studentsAge22.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(studentsAge22.getBody()).size()).isEqualTo(1);
        Assertions.assertThat(Objects.requireNonNull(studentsAge22.getBody()).get(0).getName()).isEqualTo(student3.getName());
    }


    @Test
    @Order(6)
    void testFindByAgeBetween() {
        facultyRepository.save(faculties.get(0));
        for (Student student : students) {
            studentRepository.save(student);
        }

        String url = "http://localhost:" + localServerPort + "/student/age-range?min=20&max=40";

        ResponseEntity<List> response = testRestTemplate.getForEntity(url, List.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isGreaterThanOrEqualTo(3);
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
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(testFaculty.getName());

        studentRepository.delete(savedStudent);
    }

}