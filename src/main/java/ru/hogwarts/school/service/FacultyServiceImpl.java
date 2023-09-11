package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
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

    public Faculty findFaculty(Long id) {
        return repository.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        return repository.save(faculty);
    }

    public void removeFaculty(long id) {
        repository.deleteById(id);
    }

    public List<Faculty> getFacultyByColor(String color) {
        return repository.findAll().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
