CREATE TABLE People (
   person_id SERIAL PRIMARY KEY,
   name VARCHAR(255),
   age INTEGER,
   has_license BOOLEAN
);

CREATE TABLE Cars (
   car_id SERIAL PRIMARY KEY,
   brand VARCHAR(255),
   model VARCHAR(255),
   cost DECIMAL(10, 2)
);

CREATE TABLE PersonCars (
   person_car_id SERIAL PRIMARY KEY,
   person_id INTEGER REFERENCES People(person_id),
   car_id INTEGER REFERENCES Cars(car_id)
);