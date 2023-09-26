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

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FacultyControllerRestTemplateTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private static List<Faculty> faculties;
    private static Long facultyId;

    @BeforeAll
    public static void setup() {

        faculties = new ArrayList<>();
        Faculty faculty1 = new Faculty();
        faculty1.setName("TestFaculty1");
        faculty1.setColor("TestColor1");
        faculties.add(faculty1);

        Faculty faculty2 = new Faculty();
        faculty2.setName("TestFaculty2");
        faculty2.setColor("TestColor2");
        faculties.add(faculty2);
    }

    @AfterEach
    public void cleanup() {
        for (Faculty faculty : faculties) {
            facultyRepository.delete(faculty);
        }
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    @Order(1)
    public void testAddFaculty() throws Exception {
        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFaculty");
        testFaculty.setColor("TestColor");

        Faculty savedTestFaculty = this.testRestTemplate.postForObject("http://localhost:" + localServerPort +
                "/faculty", testFaculty, Faculty.class);
        Assertions.assertThat(savedTestFaculty).isNotNull();
        Assertions.assertThat(savedTestFaculty.getId()).isNotNull();
        Assertions.assertThat(savedTestFaculty.getName()).isEqualTo(testFaculty.getName());
        Assertions.assertThat(savedTestFaculty.getColor()).isEqualTo(testFaculty.getColor());

        facultyId = savedTestFaculty.getId();
    }


    @Test
    @Order(2)
    void testFindFaculty() {
        Assertions
                .assertThat(this.testRestTemplate.getForObject("http://localhost:" + localServerPort +
                        "/faculty/1", String.class))
                .isNotNull();
    }

    @Test
    @Order(3)
    void testUpdateFaculty() {
        if (facultyId == null) {
            throw new ElementNotExistException("Такого факультета нет.");
        }

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(facultyId);
        updatedFaculty.setName("UpdatedFaculty");
        updatedFaculty.setColor("UpdatedColor");

        ResponseEntity<Faculty> response = this.testRestTemplate.exchange("http://localhost:" +
                localServerPort + "/faculty", HttpMethod.PUT, new HttpEntity<>(updatedFaculty), Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("UpdatedFaculty");
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("UpdatedColor");
    }

    @Test
    @Order(4)
    void TestRemoveFaculty() {
        if (facultyId == null) {
            throw new ElementNotExistException("Такого факультета нет.");
        }

        this.testRestTemplate.delete("http://localhost:" + localServerPort + "/faculty/" + facultyId);

        Optional<Faculty> deletedFaculty = facultyRepository.findById(facultyId);
        Assertions.assertThat(deletedFaculty).isEmpty();
    }

    @Test
    @Order(5)
    void testGetFacultyByColor() {
        Faculty testFaculty1 = facultyRepository.save(faculties.get(0));

        ResponseEntity<List<Faculty>> response = this.testRestTemplate.exchange("http://localhost:" + localServerPort +
                        "/faculty/color?color=" + testFaculty1.getColor(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty();
        Assertions.assertThat(response.getBody().size()).isEqualTo(1);
        Assertions.assertThat(response.getBody().get(0).getName()).isEqualTo("TestFaculty1");
        Assertions.assertThat(response.getBody().get(0).getColor()).isEqualTo("TestColor1");
    }


    @Test
    @Order(6)
    void testFindByNameIgnoreCaseOrColorIgnoreCase() {

        Faculty testFaculty1 = new Faculty();
        testFaculty1.setName("TestFaculty1");
        testFaculty1.setColor("TestColor1");
        facultyRepository.save(testFaculty1);
        Faculty testFaculty2 = new Faculty();
        testFaculty2.setName("TestFaculty2");
        testFaculty2.setColor("TestColor2");
        facultyRepository.save(testFaculty2);

        ResponseEntity<List<Faculty>> responseName = this.testRestTemplate.exchange("http://localhost:" +
                        localServerPort + "/faculty?nameOrColor=TeStFaCuLtY1", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });
        Assertions.assertThat(responseName.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseName.getBody()).isNotEmpty();
        Assertions.assertThat(responseName.getBody().size()).isEqualTo(1);
        Assertions.assertThat(responseName.getBody().get(0).getName()).isEqualTo("TestFaculty1");

        ResponseEntity<List<Faculty>> responseColor = this.testRestTemplate.exchange("http://localhost:" +
                        localServerPort + "/faculty?nameOrColor=tEsTcOlOr2", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Faculty>>() {
                });
        Assertions.assertThat(responseColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseColor.getBody()).isNotEmpty();
        Assertions.assertThat(responseColor.getBody().size()).isEqualTo(1);
        Assertions.assertThat(responseColor.getBody().get(0).getColor()).isEqualTo("TestColor2");
    }

    @Test
    @Order(8)
    void testGetStudentsOfFaculty() {

        Faculty testFaculty = new Faculty();
        testFaculty.setName("TestFacultyStudents");
        testFaculty.setColor("TestColorStudents");
        Faculty savedTestFaculty = facultyRepository.save(testFaculty);

        Student student1 = new Student();
        student1.setName("TestStudent1");
        student1.setFaculty(savedTestFaculty);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("TestStudent2");
        student2.setFaculty(savedTestFaculty);
        studentRepository.save(student2);

        ResponseEntity<List<Student>> responseEntity = this.testRestTemplate.exchange("http://localhost:" +
                        localServerPort + "/faculty/" + savedTestFaculty.getId() + "/students", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Student> returnedStudents = responseEntity.getBody();
        Assertions.assertThat(returnedStudents).isNotEmpty();
        Assertions.assertThat(returnedStudents.size()).isEqualTo(2);
        Assertions.assertThat(returnedStudents.get(0).getName()).isEqualTo("TestStudent1");
        Assertions.assertThat(returnedStudents.get(1).getName()).isEqualTo("TestStudent2");

        studentRepository.delete(student1);
        studentRepository.delete(student2);
        facultyRepository.delete(savedTestFaculty);
    }
}