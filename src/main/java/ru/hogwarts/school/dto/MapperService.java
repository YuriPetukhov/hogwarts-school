package ru.hogwarts.school.dto;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;


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
    @Mapping(source = "facultyId", target = "faculty.id")
    Student toEntityStudent(StudentDTO dto);

    @InheritInverseConfiguration
    @Mapping(source = "faculty.id", target = "facultyId")
    StudentDTO toDtoStudent(Student student);

}

