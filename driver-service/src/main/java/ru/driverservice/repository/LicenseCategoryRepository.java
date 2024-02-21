package ru.driverservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.driverservice.entity.LicenseCategoryEntity;

public interface LicenseCategoryRepository extends JpaRepository<LicenseCategoryEntity, Long> {
}
