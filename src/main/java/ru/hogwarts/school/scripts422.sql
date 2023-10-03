CREATE TABLE faculties (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    color VARCHAR(50)
);

CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    age INTEGER,
    faculty_id INTEGER REFERENCES faculties(id)
);

CREATE TABLE avatars (
    id SERIAL PRIMARY KEY,
    filePath VARCHAR(255),
    fileSize BIGINT,
    mediaType VARCHAR(255),
    data BYTEA,
    student_id INTEGER UNIQUE REFERENCES students(id)
);
