package ru.hogwarts.school.dto;

import lombok.Data;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@Component
@Mapper(componentModel = "spring")
public interface MapperService {

    Faculty toEntityFacultyGeneral(FacultyGeneralDTO dto);

    @InheritInverseConfiguration
    FacultyGeneralDTO toDtoFacultyGeneral(Faculty faculty);
    Faculty toEntityFaculty(FacultyDTO dto);

    @InheritInverseConfiguration
    FacultyDTO toDtoFaculty(Faculty faculty);

    Student toEntityStudentCreate(StudentCreateDTO dto);

    @InheritInverseConfiguration
    StudentCreateDTO toDtoStudentCreate(Student student);
    Student toEntityStudent(StudentDTO dto);

    @InheritInverseConfiguration
    StudentDTO toDtoStudent(Student student);
}

