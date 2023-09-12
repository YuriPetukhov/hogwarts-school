package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;

    public FacultyServiceImpl(FacultyRepository repository) {
        this.repository = repository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return repository.save(faculty);
    }

    public Faculty findFaculty(Long id) throws ElementNotExistException {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));
    }

    public Faculty updateFaculty(Faculty faculty) throws ElementNotExistException {
        if(!repository.existsById(faculty.getId())){
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        return repository.save(faculty);
    }

    public void removeFaculty(long id) throws ElementNotExistException {
        if(!repository.existsById(id)) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        repository.deleteById(id);
    }

    public List<Faculty> getFacultyByColor(String color) {
        return repository.findAllByColor(color);
    }
}
