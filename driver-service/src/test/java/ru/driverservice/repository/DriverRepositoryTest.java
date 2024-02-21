package ru.driverservice.repository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import ru.driverservice.entity.DriverEntity;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testFindByFirstNameAndLastName() {
        DriverEntity driverEntity = DriverEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Some Address, City, 123456")
                .dateOfBirth(LocalDate.of(1997, 10, 1))
                .salary(50000)
                .build();
        entityManager.persist(driverEntity);
        entityManager.flush();

        Optional<DriverEntity> foundDriver = driverRepository.findByFirstNameAndLastName("John", "Doe");

        assertTrue(foundDriver.isPresent());
        assertEquals("john.doe@example.com", foundDriver.get().getEmail());
        assertEquals("Some Address, City, 123456", foundDriver.get().getAddress());
        assertEquals(50000, foundDriver.get().getSalary());
    }

    @Test
    @Transactional
    public void testFindDrivers() {
        DriverEntity driverEntity1 = DriverEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("Some Address, City, 123456")
                .dateOfBirth(LocalDate.of(1997, 10, 1))
                .salary(50000)
                .build();

        DriverEntity driverEntity2 = DriverEntity.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .address("Another Address, City, 789012")
                .dateOfBirth(LocalDate.of(1998, 9, 15))
                .salary(60000)
                .build();

        entityManager.persist(driverEntity1);
        entityManager.persist(driverEntity2);
        entityManager.flush();

        Page<DriverEntity> foundDrivers = driverRepository.findDrivers(
                "John", null, 0, null,
                null, null, PageRequest.of(0, 10));

        assertEquals(1, foundDrivers.getTotalElements());
        assertEquals("john.doe@example.com", foundDrivers.getContent().get(0).getEmail());
        assertEquals("Some Address, City, 123456", foundDrivers.getContent().get(0).getAddress());
        assertEquals(50000, foundDrivers.getContent().get(0).getSalary());
    }
}
