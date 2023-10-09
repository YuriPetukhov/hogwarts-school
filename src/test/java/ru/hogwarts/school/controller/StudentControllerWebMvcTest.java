package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.dto.MapperService;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private StudentServiceImpl studentService;
    @SpyBean
    private FacultyServiceImpl facultyService;
    private StudentController studentController;

    @Test
    void testAddAndFindStudent() throws Exception {
        final Long id = 1L;
        final String name = "Student1";
        final int age = 24;

        final Long facultyId = 2L;
        final String facultyName = "Faculty2";
        final String facultyColor = "Color2";

        final Faculty faculty = new Faculty();
        faculty.setName(facultyName);
        faculty.setId(facultyId);
        faculty.setColor(facultyColor);
        final List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student1 = new Student();
        student1.setId(id);
        student1.setName(name);
        student1.setAge(age);
        student1.setFaculty(faculty);


        when(studentRepository.save(any(Student.class))).thenReturn(student1);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student1));
        when(facultyRepository.findAll()).thenReturn(faculties);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.facultyId").value(facultyId));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    void testUpdateStudent() throws Exception {

        final Long id = 1L;
        final String updatedName = "Student1 - Updated";
        final int updatedAge = 25;

        JSONObject studentObject = new JSONObject();
        studentObject.put("id", id);
        studentObject.put("name", updatedName);
        studentObject.put("age", updatedAge);

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setName(updatedName);
        updatedStudent.setAge(updatedAge);

        when(studentRepository.existsById(any(Long.class))).thenReturn(true);
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.age").value(updatedAge));
    }

    @Test
    void testRemoveStudent() throws Exception {

        Long id = 1L;
        when(studentRepository.existsById(id)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetStudentsByAge() throws Exception {

        final int age = 14;
        final Student student1 = new Student();

        student1.setId(1L);
        student1.setName("Student1");
        student1.setAge(age);

        when(studentService.getStudentsByAge(age)).thenReturn(Collections.singletonList(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age")
                        .param("age", String.valueOf(age))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student1.getId()))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()));
    }

    @Test
    void testFindByAgeBetween() throws Exception {

        int min = 24;
        int max = 26;
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Student1");
        student1.setAge(25);

        when(studentService.findByAgeBetween(min, max)).thenReturn(Collections.singletonList(student1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/age-range")
                        .param("min", Integer.toString(min))
                        .param("max", Integer.toString(max))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(student1.getId()))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()));
    }

    @Test
    void testGetFacultyOfStudent() throws Exception {

        final Long studentId = 1L;

        final Long facultyId = 2L;
        final String facultyName = "Faculty1";
        final String color = "Color1";

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        Student student = new Student();
        student.setId(studentId);
        student.setFaculty(faculty);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + studentId + "/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(color));
    }
}