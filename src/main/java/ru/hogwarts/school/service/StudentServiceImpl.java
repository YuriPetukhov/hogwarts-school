package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{
    private final StudentRepository repository;

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        return repository.save(student);
    }
    public Student findStudent(Long id) {
        return repository.findById(id).get();
    }
    public Student updateStudent(Student student) {
        return repository.save(student);
    }
    public void removeStudent(long id) {
       repository.deleteById(id);
    }

    public List<Student> getStudentsByAge(int age) {
        return repository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
