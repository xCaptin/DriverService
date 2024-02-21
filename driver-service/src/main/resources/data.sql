INSERT INTO license (issue_date, expiration_date, identification_number)
VALUES ('2020-01-01', '2025-01-01', 'ABC1234567');

INSERT INTO license_category (category, license_id)
VALUES ('CATEGORY_A', 1), ('CATEGORY_B', 1);

INSERT INTO driver (first_name, last_name, father_name, date_of_birth, age, email, address, salary, license_id)
VALUES ('John', 'Doe', 'Father Doe', '1990-01-01', 32, 'john.doe@example.com', 'Some Address, City, 123456', 50000, 1);

