package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private static final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Override
    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked method for adding faculty");
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for finding faculty");
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not faculty with id = " + id);
                    return new ElementNotExistException("Такого факультета нет в базе");
                });
    }

    @Override
    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Was invoked method for updating faculty");
        if (!facultyRepository.existsById(id)) {
            logger.error("There is not faculty with id = " + id);
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public void removeFaculty(long id) {
        logger.info("Was invoked method for removing faculty");
        if (!facultyRepository.existsById(id)) {
            logger.error("There is not faculty with id = " + id);
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        facultyRepository.deleteById(id);
    }

    @Override
    public List<Faculty> getFacultyByColor(String color) {
        logger.info("Was invoked method for getting faculty by color");
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        logger.info("Was invoked method for finding by name or color");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    @Override
    public List<Student> getStudentsOfFaculty(Long id){
        logger.info("Was invoked method for getting students of faculty");
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not faculty with id = " + id);
                    return new ElementNotExistException("Такого факультета нет в базе");
                });
        return faculty.getStudents();
    }

    @Override
    public List<Faculty> findAll() {
        logger.info("Was invoked method for finding all");
        return facultyRepository.findAll();
    }

    @Override
    public Optional<Faculty> findById(Long id) {
        logger.info("Was invoked method for finding by id");
        return facultyRepository.findById(id);
    }
}
