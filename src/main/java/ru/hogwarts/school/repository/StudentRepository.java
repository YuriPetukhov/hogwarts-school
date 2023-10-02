package ru.hogwarts.school.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findStudentByAge(int age);

    List<Student> findByAgeBetween(int min, int max);
    @Query("SELECT COUNT(s) FROM students s")
    Long countAllStudents();

    @Query("SELECT AVG(s.age) FROM students s")
    Double getAverageAge();

    @Query("SELECT s FROM students s ORDER BY s.id DESC")
    List<Student> findLastFiveStudents(PageRequest of);
}
