package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {
    private FacultyService facultyService;

    @BeforeEach
    void setUp() {
        facultyService = new FacultyService();
    }

    @Test
    void shouldSaveAndReturnFacultyWithId() {
        Faculty faculty = new Faculty(null, "Gryffindor", "Red");

        Faculty addedFaculty = facultyService.addFaculty(faculty);
        assertNotNull(addedFaculty);
        assertEquals(1L, addedFaculty.getId());
    }

    @Test
    void shouldFindAndReturnFacultyById() {
        Faculty faculty = new Faculty(null, "Slytherin", "Green");

        Faculty addedFaculty = facultyService.addFaculty(faculty);
        assertEquals(1L, addedFaculty.getId());

        Faculty foundFaculty = facultyService.findFaculty(1L);
        assertNotNull(foundFaculty);
        assertEquals("Slytherin", foundFaculty.getName());
        assertEquals("Green", foundFaculty.getColor());
    }

    @Test
    void shouldUpdateAndReturnModifiedFaculty() {
        Faculty faculty = new Faculty(null, "Hufflepuff", "Yellow");

        facultyService.addFaculty(faculty);

        Faculty updatedFaculty = new Faculty(1L, "Hufflepuff", "Orange");

        facultyService.updateFaculty(updatedFaculty);
        Faculty foundUpdatedFaculty = facultyService.findFaculty(1L);

        assertNotNull(foundUpdatedFaculty);
        assertEquals("Hufflepuff", foundUpdatedFaculty.getName());
        assertEquals("Orange", foundUpdatedFaculty.getColor());
    }

    @Test
    void shouldDeleteFacultyById() {
        Faculty faculty = new Faculty(null, "Ravenclaw", "Blue");

        Faculty addedFaculty = facultyService.addFaculty(faculty);
        assertEquals(1L, addedFaculty.getId());

        Faculty removedFaculty = facultyService.removeFaculty(1L);
        assertNotNull(removedFaculty);
        assertNull(facultyService.findFaculty(1L));
    }

    @Test
    void shouldReturnAllFacultyByColor() {
        Faculty redFaculty1 = new Faculty(null, "Gryffindor", "Red");
        facultyService.addFaculty(redFaculty1);

        Faculty redFaculty2 = new Faculty(null, "Cornelius", "Red");
        facultyService.addFaculty(redFaculty2);

        Faculty greenFaculty = new Faculty(null, "Slytherin", "Green");
        facultyService.addFaculty(greenFaculty);

        List<Faculty> redFaculties = facultyService.getFacultyByColor("Red");
        assertEquals(2, redFaculties.size());
        assertTrue(redFaculties.stream().anyMatch(f -> f.getName().equals("Gryffindor")));
        assertTrue(redFaculties.stream().anyMatch(f -> f.getName().equals("Cornelius")));
    }
}
