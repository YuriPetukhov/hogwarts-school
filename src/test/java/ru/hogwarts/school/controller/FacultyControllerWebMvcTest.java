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
import ru.hogwarts.school.dto.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.impl.FacultyServiceImpl;
import ru.hogwarts.school.service.impl.StudentServiceImpl;

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
    @MockBean
    private MapperService mapperService;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private FacultyServiceImpl facultyService;
    @SpyBean
    private StudentServiceImpl studentService;
    private FacultyController facultyController;

    @Test
    void testAddAndFindFaculty() throws Exception {
        final Long id = 1L;
        final String name = "Faculty1";
        final String color = "Color1";

        JSONObject facultyGeneralDTOObject = new JSONObject();
        facultyGeneralDTOObject.put("name", name);
        facultyGeneralDTOObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        FacultyGeneralDTO facultyGeneralDTO = new FacultyGeneralDTO();
        facultyGeneralDTO.setId(id);
        facultyGeneralDTO.setName(name);
        facultyGeneralDTO.setColor(color);

        when(mapperService.toEntityFacultyGeneral(any(FacultyGeneralDTO.class))).thenReturn(faculty);
        when(mapperService.toDtoFacultyGeneral(faculty)).thenReturn(facultyGeneralDTO);

        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setId(id);
        facultyDTO.setName(name);
        facultyDTO.setColor(color);

        when(mapperService.toEntityFaculty(any(FacultyDTO.class))).thenReturn(faculty);
        when(mapperService.toDtoFaculty(faculty)).thenReturn(facultyDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyGeneralDTOObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
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

        JSONObject facultyGeneralDTOObject = new JSONObject();
        facultyGeneralDTOObject.put("id", id);
        facultyGeneralDTOObject.put("name", updatedName);
        facultyGeneralDTOObject.put("color", updatedColor);

        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(id);
        updatedFaculty.setName(updatedName);
        updatedFaculty.setColor(updatedColor);

        FacultyGeneralDTO facultyGeneralDTO = new FacultyGeneralDTO();
        facultyGeneralDTO.setId(id);
        facultyGeneralDTO.setName(updatedName);
        facultyGeneralDTO.setColor(updatedColor);

        when(mapperService.toEntityFacultyGeneral(any(FacultyGeneralDTO.class))).thenReturn(updatedFaculty);
        when(mapperService.toDtoFacultyGeneral(updatedFaculty)).thenReturn(facultyGeneralDTO);

        when(facultyRepository.existsById(any(Long.class))).thenReturn(true);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/" + id)
                        .content(facultyGeneralDTOObject.toString())
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

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204));
    }


    @Test
    void getFacultyByColor() throws Exception {
        final Long id = 1L;
        final String name = "Faculty1";
        final String color = "Color1";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        FacultyGeneralDTO facultyGeneralDTO = new FacultyGeneralDTO();
        facultyGeneralDTO.setId(id);
        facultyGeneralDTO.setName(name);
        facultyGeneralDTO.setColor(color);

        when(mapperService.toEntityFacultyGeneral(any(FacultyGeneralDTO.class))).thenReturn(faculty);
        when(mapperService.toDtoFacultyGeneral(faculty)).thenReturn(facultyGeneralDTO);

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

        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setId(id);
        facultyDTO.setName(name);
        facultyDTO.setColor(color);

        when(mapperService.toEntityFaculty(any(FacultyDTO.class))).thenReturn(faculty);
        when(mapperService.toDtoFaculty(faculty)).thenReturn(facultyDTO);

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
        final int age = 20;

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        Student student = new Student();
        student.setId(studentId);
        student.setName(studentName);
        student.setAge(age);
        student.setFaculty(faculty);

        faculty.setStudents(Collections.singletonList(student));

        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setId(facultyId);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(studentId);
        studentDTO.setName(studentName);
        studentDTO.setAge(age);
        studentDTO.setFacultyId(facultyId);

        facultyDTO.setStudents(Collections.singletonList(studentDTO));

        when(mapperService.toEntityStudent(any(StudentDTO.class))).thenReturn(student);
        when(mapperService.toDtoStudent(student)).thenReturn(studentDTO);

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