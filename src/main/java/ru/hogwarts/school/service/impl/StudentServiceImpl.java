package ru.hogwarts.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.ElementNotExistException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final FacultyService facultyService;

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private Long printCounter = 1L;

    @Override
    public Student addStudent(Student student) {
        logger.info("Was invoked addStudent method");
        student.setFaculty(selectRandomFaculty());
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findStudent(Long id) {
        logger.info("Was invoked findStudent method");
        return studentRepository.findById(id);
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        logger.info("Was invoked updateStudent method");
        if (!studentRepository.existsById(id)) {
            logger.warn("There is not student with id = " + id);
            throw new ElementNotExistException("Такого студента нет в базе");
        }
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty existingFaculty = facultyService.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new ElementNotExistException("Факультет не найден"));
            student.setFaculty(existingFaculty);
        }

        return studentRepository.save(student);
    }

    @Override
    public void removeStudent(long id) {
        logger.info("Was invoked removeStudent method");
        if (!studentRepository.existsById(id)) {
            logger.error("There is not student with id = " + id);
            throw new ElementNotExistException("Такого студента нет в базе");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked getStudentsByAge method");
        return studentRepository.findStudentByAge(age);
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked findByAgeBetween method");
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyOfStudent(Long studentId) {
        logger.info("Was invoked getFacultyOfStudent method");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ElementNotExistException("Такого студента нет в базе"));
        return student.getFaculty();
    }

    @Override
    public Faculty selectRandomFaculty() {
        logger.info("Was invoked selectRandomFaculty method");
        List<Faculty> faculties = facultyService.findAll();
        if (faculties.isEmpty()) {
            logger.warn("There are no available faculties");
            throw new ElementNotExistException("Нет доступных факультетов");
        }
        int randomIndex = new Random().nextInt(faculties.size());
        return faculties.get(randomIndex);
    }

    @Override
    public Long countAllStudents() {
        logger.info("Was invoked countAllStudents method");
        return studentRepository.countAllStudents();
    }

    @Override
    public Double getAverageAge() {
        logger.info("Was invoked getAverageAge method");
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> findLastFiveStudents() {
        logger.info("Was invoked findLastFiveStudents method");
        PageRequest of = PageRequest.of(0, 5);
        return studentRepository.findLastFiveStudents(of);
    }

    @Override
    public List<String> findAllStudentsByFirstLetter(Character firstLetter) {
        logger.info("Was invoked findAllStudentsByFirstLetter method");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> Character.toUpperCase(name.charAt(0)) == Character.toUpperCase(firstLetter))
                .map(String::toUpperCase)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    @Override
    public Double getAverageAgeInStream() {
        logger.info("Was invoked getAverageAgeInStream method");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public void printNamesToConsole() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            throw new ElementNotExistException("Нет студентов в базе");
        }
        int count = 0;
        while (students.size() > count) {
            if ((students.size() - count) / 6 == 0) {
                for (int i = count; i < students.size(); i++) {
                    printName(students.get(i).getName());
                }
                return;
            } else {

                int finalCount = count;

                printName(students.get(finalCount).getName());
                printName(students.get(finalCount + 1).getName());

                new Thread(() -> {
                    printName(students.get(finalCount + 2).getName());
                    printName(students.get(finalCount + 3).getName());
                }).start();
                new Thread(() -> {
                    printName(students.get(finalCount + 4).getName());
                    printName(students.get(finalCount + 5).getName());
                }).start();
            }
            count = count + 6;

        }
    }

    @Override
    public void printNamesToConsoleSynchronized() {
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            throw new ElementNotExistException("Нет студентов в базе");
        }

        int count = 0;
        while (students.size() > count) {
            if ((students.size() - count) / 6 == 0) {
                for (int i = count; i < students.size(); i++) {
                    printNameSynchronized(students.get(i).getName());
                }
                return;
            } else {

                int finalCount = count;

                printNameSynchronized(students.get(finalCount).getName());
                printNameSynchronized(students.get(finalCount + 1).getName());

                Thread thread1 = new Thread(() -> {
                    printNameSynchronized(students.get(finalCount + 2).getName());
                    printNameSynchronized(students.get(finalCount + 3).getName());
                });

                Thread thread2 = new Thread(() -> {
                    printNameSynchronized(students.get(finalCount + 4).getName());
                    printNameSynchronized(students.get(finalCount + 5).getName());
                });

                thread1.start();
                thread2.start();

                try {
                    thread1.join();
                    thread2.join();
                } catch (InterruptedException e) {
                    System.err.println("Поток был прерван");
                }
            }

            count = count + 6;

        }
    }

    private void printName(String name) {
        System.out.println("Имя студента: " + name);
    }

    private synchronized void printNameSynchronized(String name) {
        System.out.println(printCounter + " Имя студента: " + name);
        if (Objects.equals(printCounter, studentRepository.countAllStudents())) {
            printCounter = 1L;
        } else {
            printCounter++;
        }
    }

    private boolean isValidStudent(Student student) {
        logger.debug("Checking if student is valid in isValidStudent method");
        return student.getName() != null && student.getAge() >= 16;
    }
}
