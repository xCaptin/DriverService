CREATE TABLE IF NOT EXISTS driver (
                                      id SERIAL PRIMARY KEY,
                                      first_name VARCHAR(20) NOT NULL,
                                      last_name VARCHAR(20) NOT NULL,
                                      father_name VARCHAR(20),
                                      date_of_birth DATE NOT NULL,
                                      age INTEGER,
                                      email VARCHAR(255) NOT NULL UNIQUE,
                                      address VARCHAR(100) NOT NULL UNIQUE,
                                      salary INTEGER NOT NULL,
                                      license_id BIGINT,
                                      FOREIGN KEY (license_id) REFERENCES license(id)
);

CREATE TABLE IF NOT EXISTS license (
                                       id SERIAL PRIMARY KEY,
                                       issue_date DATE NOT NULL,
                                       expiration_date DATE NOT NULL,
                                       identification_number VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS license_category (
                                                id SERIAL PRIMARY KEY,
                                                category VARCHAR(10) NOT NULL,
                                                license_id BIGINT,
                                                FOREIGN KEY (license_id) REFERENCES license(id)
);
