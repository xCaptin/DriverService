package ru.driverservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.driverservice.entity.DriverEntity;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {

    @Query("SELECT d FROM DriverEntity d " +
            "WHERE (:firstName IS NULL OR d.firstName LIKE %:firstName%) " +
            "AND (:lastName IS NULL OR d.lastName LIKE %:lastName%) " +
            "AND (:minSalary IS NULL OR d.salary >= :minSalary) " +
            "AND (:maxSalary IS NULL OR d.salary <= :maxSalary) " +
            "AND (:minAge IS NULL OR d.age >= :minAge) " +
            "AND (:maxAge IS NULL OR d.age <= :maxAge)")
    Page<DriverEntity> findDrivers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("minSalary") Integer minSalary,
            @Param("maxSalary") Integer maxSalary,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            Pageable pageable);

    Optional<DriverEntity> findByFirstNameAndLastName(String firstName, String lastName);
}

