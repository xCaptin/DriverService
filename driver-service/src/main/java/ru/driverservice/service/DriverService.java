package ru.driverservice.service;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.driverservice.dto.DriverDto;
import ru.driverservice.dto.LicenseCategoryDto;
import ru.driverservice.dto.LicenseDto;
import ru.driverservice.entity.DriverEntity;
import ru.driverservice.entity.LicenseCategoryEntity;
import ru.driverservice.entity.LicenseEntity;
import ru.driverservice.exception.NotFoundException;
import ru.driverservice.mapper.DriverMapper;
import ru.driverservice.mapper.LicenseCategoryMapper;
import ru.driverservice.mapper.LicenseMapper;
import ru.driverservice.repository.DriverRepository;
import ru.driverservice.repository.LicenseRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DriverService {

    DriverRepository driverRepository;
    DriverMapper driverMapper;
    LicenseRepository licenseRepository;
    LicenseMapper licenseMapper;
    LicenseCategoryMapper licenseCategoryMapper;

    public DriverEntity getDriverById(Long id) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver", "id", id));

        DriverDto driverDto = driverMapper.driverToDriverDto(driverEntity);
        if (driverEntity.getLicense() != null) {
            LicenseDto licenseDto = licenseMapper.licenseToLicenseDto(driverEntity.getLicense());
            Set<LicenseCategoryDto> categoryDtos = driverEntity.getLicense().getCategories().stream()
                    .map(licenseCategoryMapper::licenseCategoryToLicenseCategoryDto)
                    .collect(Collectors.toSet());

            if (licenseDto != null) {
                licenseDto = licenseDto.withCategories(categoryDtos);
                driverDto = driverDto.withLicense(licenseDto);
            }
        }
        return driverMapper.driverDtoToDriver(driverDto);
    }

    @Transactional
    public DriverEntity createDriver(@Valid DriverDto driverDto) {
        DriverEntity driverEntity = driverMapper.driverDtoToDriver(driverDto);

        driverEntity.setAge(calculateAge(driverDto.dateOfBirth()));

        if (driverDto.license() != null) {
            LicenseEntity licenseEntity = createLicenseEntityFromDto(driverDto.license());
            driverEntity.setLicense(licenseEntity);

            licenseEntity = licenseRepository.save(licenseEntity);
        }

        return driverRepository.save(driverEntity);
    }


    @Transactional
    public DriverEntity updateDriver(Long id, @Valid DriverDto driverDto) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver", "id", id));

        driverMapper.updateDriverFromDto(driverDto, driverEntity);

        driverEntity.setAge(calculateAge(driverDto.dateOfBirth()));

        if (driverDto.license() != null) {
            LicenseEntity existingLicenseEntity = driverEntity.getLicense();
            if (existingLicenseEntity == null) {
                LicenseEntity licenseEntity = createLicenseEntityFromDto(driverDto.license());
                driverEntity.setLicense(licenseEntity);
            } else {
                updateLicenseEntityFromDto(driverDto.license(), existingLicenseEntity);
            }
        }
        driverEntity.setId(id);
        return driverRepository.save(driverEntity);
    }

    public DriverEntity deleteDriver(Long id) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver", "id", id));

        driverRepository.deleteById(id);

        return driverEntity;
    }

    public Page<DriverEntity> getDriversByFilter(String firstName, String lastName, Integer minSalary, Integer maxSalary,
                                                 Integer minAge, Integer maxAge, Pageable pageable) {
        return driverRepository.findDrivers(firstName, lastName, minSalary, maxSalary, minAge, maxAge, pageable);
    }

    private int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }

    private LicenseEntity createLicenseEntityFromDto(LicenseDto licenseDto) {
        LicenseEntity licenseEntity = licenseMapper.licenseDtoToLicense(licenseDto);
        Set<LicenseCategoryEntity> categoryEntities = licenseDto.categories().stream()
                .map(categoryDto -> {
                    LicenseCategoryEntity categoryEntity = licenseCategoryMapper.licenseCategoryDtoToLicenseCategory(categoryDto);
                    categoryEntity.setLicense(licenseEntity);
                    return categoryEntity;
                })
                .collect(Collectors.toSet());
        licenseEntity.setCategories(categoryEntities);
        return licenseEntity;
    }

    private void updateLicenseEntityFromDto(LicenseDto licenseDto, LicenseEntity existingLicenseEntity) {
        licenseMapper.updateLicenseFromDto(licenseDto, existingLicenseEntity);

        existingLicenseEntity.getCategories().clear();

        Set<LicenseCategoryEntity> updatedCategories = licenseDto.categories().stream()
                .map(categoryDto -> {
                    LicenseCategoryEntity categoryEntity = licenseCategoryMapper.licenseCategoryDtoToLicenseCategory(categoryDto);
                    categoryEntity.setLicense(existingLicenseEntity);
                    return categoryEntity;
                })
                .collect(Collectors.toSet());
        existingLicenseEntity.getCategories().addAll(updatedCategories);
    }

}
