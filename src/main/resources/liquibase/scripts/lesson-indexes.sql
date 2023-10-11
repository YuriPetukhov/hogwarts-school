--liquibase formatted SQL

--changeset yuripetukhov:add_indexes_student_name
CREATE INDEX student_name_index_name ON students (name);

--changeset yuripetukhov:add_indexes_faculty_color_name
CREATE INDEX faculty_name_color_index_name ON faculties (name, color);
