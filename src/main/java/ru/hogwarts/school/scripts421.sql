ALTER TABLE students
ADD CONSTRAINT check_age CHECK (age >= 16);

ALTER TABLE students
ADD CONSTRAINT unique_name CHECK (name IS NOT NULL AND name <> ''),
ADD CONSTRAINT unique_name_value UNIQUE (name);

ALTER  TABLE students
ALTER COLUMN name SET NOT NULL;

ALTER TABLE faculties
ADD CONSTRAINT unique_name_color UNIQUE (name, color);

ALTER TABLE students
ALTER COLUMN age SET DEFAULT 20;