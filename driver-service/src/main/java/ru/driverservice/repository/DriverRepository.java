package ru.driverservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.driverservice.entity.DriverEntity;

public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    Page<DriverEntity> findAllByFirstNameContaining(String firstName, Pageable pageable);

    Page<DriverEntity> findAllByLastNameContaining(String lastName, Pageable pageable);

    Page<DriverEntity> findAllByFirstNameAndLastNameContaining(String firstName, String lastName, Pageable pageable);
}
