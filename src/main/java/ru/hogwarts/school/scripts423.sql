SELECT s.name, s.age, f.name
FROM students s
JOIN faculties f ON s.faculty_id = f.id;

SELECT s.name, s.age, f.name
FROM students s
JOIN faculties f ON s.faculty_id = f.id
JOIN avatars a ON s.id = a.student_id