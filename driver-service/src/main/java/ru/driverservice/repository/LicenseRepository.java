package ru.driverservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.driverservice.entity.LicenseEntity;

public interface LicenseRepository extends JpaRepository<LicenseEntity, Long> {
}
