package ru.driverservice.initializer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.driverservice.entity.DriverEntity;
import ru.driverservice.repository.DriverRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Profile("!test")
@Component
public class DataInitializer implements ApplicationRunner {

    JdbcTemplate jdbcTemplate;
    DriverRepository driverRepository;

    public DataInitializer(JdbcTemplate jdbcTemplate, DriverRepository driverRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.driverRepository = driverRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
//        createDrivers();
        updateDrivers();
    }

//    private void createDrivers() {
//        if (!driverExists("John", "Doe")) {
//            String sql = "INSERT INTO license (issue_date, expiration_date, identification_number) " +
//                    "VALUES ('2020-01-01', '2025-01-01', 'ABC1234567'); " +
//                    "INSERT INTO license_category (category, license_id) " +
//                    "VALUES ('CATEGORY_A', 1), ('CATEGORY_B', 1); " +
//                    "INSERT INTO driver (first_name, last_name, father_name, date_of_birth, email, address, salary, license_id) " +
//                    "VALUES ('John', 'Doe', 'Father Doe', '20-02-1999', 'john.doe@example.com', 'Some Address, City, 123456', 55000, 1);";
//
//            jdbcTemplate.execute(sql);
//        }
//    }

    private boolean driverExists(String firstName, String lastName) {
        Optional<DriverEntity> existingDriver = driverRepository.findByFirstNameAndLastName(firstName, lastName);
        return existingDriver.isPresent();
    }

    private void updateDrivers() {
        List<DriverEntity> drivers = driverRepository.findAll();

        for (DriverEntity driver : drivers) {
            LocalDate dateOfBirth = driver.getDateOfBirth();
            int currentAge = calculateAge(dateOfBirth);
            driver.setAge(currentAge);
        }

        driverRepository.saveAll(drivers);
    }
    private int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }
}
