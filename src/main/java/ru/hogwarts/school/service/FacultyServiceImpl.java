package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repository;

    public FacultyServiceImpl(FacultyRepository repository) {
        this.repository = repository;
    }
    @Override
    public Faculty addFaculty(Faculty faculty) {
        return repository.save(faculty);
    }
    @Override
    public Faculty findFaculty(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ElementNotExistException("Такого факультета нет в базе"));
    }
    @Override
    public Faculty updateFaculty(Faculty faculty) {
        if (!repository.existsById(faculty.getId())) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        return repository.save(faculty);
    }
    @Override
    public void removeFaculty(long id) {
        if (!repository.existsById(id)) {
            throw new ElementNotExistException("Такого факультета нет в базе");
        }
        repository.deleteById(id);
    }
    @Override
    public List<Faculty> getFacultyByColor(String color) {
        return repository.findAllByColor(color);
    }
    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        return repository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }
}