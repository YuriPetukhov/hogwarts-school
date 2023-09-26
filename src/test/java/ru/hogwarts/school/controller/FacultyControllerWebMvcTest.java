package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private FacultyServiceImpl facultyService;
    private FacultyController facultyController;

    @Test
    void testAddAndFindFaculty() throws Exception {
        final Long id = 1L;
        final String name = "Faculty1";
        final String color = "Color1";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty1 = new Faculty();
        faculty1.setId(id);
        faculty1.setName(name);
        faculty1.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty1);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty1));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    void testUpdateFaculty() throws Exception {
        final Long id = 1L;
        final String updatedName = "Faculty1 - Updated";
        final String updatedColor = "Color1 - Updated";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", updatedName);
        facultyObject.put("color", updatedColor);

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(id);
        updatedFaculty.setName(updatedName);
        updatedFaculty.setColor(updatedColor);

        when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.color").value(updatedColor));
    }

    @Test
    void testRemoveFaculty() throws Exception {
        Long id = 1L;
        when(facultyRepository.existsById(id)).thenReturn(true);
        doNothing().when(facultyRepository).deleteById(id);

        String expectedSuccessMessage = "The faculty has been successfully removed.";

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedSuccessMessage));
    }

    @Test
    void getFacultyByColor() throws Exception {
        final Long id = 1L;
        final String updatedName = "Faculty1 - Updated";
        final String color = "Color1 - Updated";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(updatedName);
        faculty.setColor(color);

        when(facultyRepository.findAllByColor(color)).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/color")
                        .param("color", color)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(faculty.getId()))
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
    }

    @Test
    void testFindByNameIgnoreCaseOrColorIgnoreCase() throws Exception {
        final Long id = 1L;
        final String name = "Faculty";
        final String color = "Color";

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase("FACULTY", "FACULTY"))
                .thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .param("nameOrColor", "FACULTY")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));

        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase("COLOR", "COLOR"))
                .thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .param("nameOrColor", "COLOR")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));
    }

    @Test
    void testGetStudentsOfFaculty() throws Exception {
        final Long facultyId = 1L;
        final Long studentId = 1L;
        final String studentName = "Student1";
        final int age = 18;

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        Student student = new Student();
        student.setId(studentId);
        student.setName(studentName);
        student.setAge(age);
        student.setFaculty(faculty);

        faculty.setStudents(Collections.singletonList(student));

        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + facultyId + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(studentName))
                .andExpect(jsonPath("$[0].age").value(age));
    }

}