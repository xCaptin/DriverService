package ru.driverservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private LicenseRepository licenseRepository;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private LicenseMapper licenseMapper;

    @Mock
    private LicenseCategoryMapper licenseCategoryMapper;

    @InjectMocks
    private DriverService driverService;

    @Test
    void shouldGetDriverById() {
        Long driverId = 1L;

        DriverEntity mockedDriverEntity = createMockDriverEntity(driverId);
        DriverDto mockedDriverDto = createMockDriverDto(driverId);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(mockedDriverEntity));
        when(driverMapper.driverToDriverDto(mockedDriverEntity)).thenReturn(mockedDriverDto);
        when(driverMapper.driverDtoToDriver(mockedDriverDto)).thenReturn(mockedDriverEntity);

        DriverEntity actual = driverService.getDriverById(driverId);

        assertEquals(mockedDriverEntity, actual);
    }

    @Test
    void shouldThrowExceptionWhenDriverNotFoundById() {
        Long driverId = 1L;

        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> driverService.getDriverById(driverId));
    }

    @Test
    void shouldCreateDriver() {

        DriverDto driverDto = createMockDriverDto(1L);
        DriverEntity expectedDriverEntity = createMockDriverEntity(1L);

        when(driverMapper.driverDtoToDriver(driverDto)).thenReturn(expectedDriverEntity);

        LicenseDto licenseDto = driverDto.license();
        if (licenseDto != null) {
            LicenseEntity expectedLicenseEntity = createMockLicenseEntity();
            when(licenseMapper.licenseDtoToLicense(licenseDto)).thenReturn(expectedLicenseEntity);

            Set<LicenseCategoryDto> categoryDtos = licenseDto.categories();
            if (categoryDtos != null) {
                LicenseCategoryEntity expectedCategoryEntity = LicenseCategoryEntity.builder()
                        .category(LicenseCategoryEntity.CategoryType.CATEGORY_A)
                        .build();

                when(licenseCategoryMapper.licenseCategoryDtoToLicenseCategory(any())).thenReturn(expectedCategoryEntity);
            }
        }

        when(driverRepository.save(expectedDriverEntity)).thenReturn(expectedDriverEntity);

        DriverEntity actualDriverEntity = driverService.createDriver(driverDto);

        assertNotNull(actualDriverEntity);
        assertEquals(expectedDriverEntity, actualDriverEntity);
    }

    @Test
    void shouldUpdateDriver() {
        Long driverId = 1L;

        DriverEntity existingDriverEntity = createMockDriverEntity(driverId);
        DriverDto updatedDriverDto = createMockDriverDto(driverId);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(existingDriverEntity));

        when(driverMapper.driverDtoToDriver(updatedDriverDto)).thenReturn(existingDriverEntity);
        doNothing().when(driverMapper).updateDriverFromDto(updatedDriverDto, existingDriverEntity);

        LicenseDto licenseDto = updatedDriverDto.license();
        if (licenseDto != null) {
            LicenseEntity existingLicenseEntity = existingDriverEntity.getLicense();
            if (existingLicenseEntity == null) {
                LicenseEntity licenseEntity = createMockLicenseEntity();
                when(licenseMapper.licenseDtoToLicense(licenseDto)).thenReturn(licenseEntity);

                Set<LicenseCategoryDto> categoryDtos = licenseDto.categories();
                if (categoryDtos != null) {
                    LicenseCategoryEntity expectedCategoryEntity = createMockLicenseCategoryEntity();

                    when(licenseCategoryMapper.licenseCategoryDtoToLicenseCategory(any())).thenReturn(expectedCategoryEntity);

                    doNothing().when(expectedCategoryEntity).setLicense(any(LicenseEntity.class));
                }
            }
        }

        when(licenseRepository.save(any(LicenseEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DriverEntity actual = driverService.updateDriver(driverId, updatedDriverDto);

        assertEquals(existingDriverEntity, actual);
    }

    @Test
    void shouldThrowExceptionWhenUpdateDriverNotFoundById() {
        Long driverId = 1L;

        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> driverService.updateDriver(driverId, createMockDriverDto(driverId)));
    }

    @Test
    void shouldDeleteDriver() {
        Long driverId = 1L;

        DriverEntity mockedDriverEntity = createMockDriverEntity(driverId);

        when(driverRepository.findById(driverId)).thenReturn(Optional.of(mockedDriverEntity));

        DriverEntity actual = driverService.deleteDriver(driverId);

        assertEquals(mockedDriverEntity, actual);
    }

    @Test
    void shouldThrowExceptionWhenDeleteDriverNotFoundById() {
        Long driverId = 1L;

        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> driverService.deleteDriver(driverId));
    }

    private DriverEntity createMockDriverEntity(Long id) {
        Set<LicenseCategoryEntity> categoryEntities = new HashSet<>();
        LicenseCategoryEntity categoryEntity = LicenseCategoryEntity.builder()
                .category(LicenseCategoryEntity.CategoryType.CATEGORY_A)
                .build();
        categoryEntities.add(categoryEntity);

        LicenseEntity licenseEntity = LicenseEntity.builder()
                .issueDate(LocalDate.of(2022, 1, 1))
                .expirationDate(LocalDate.of(2025, 12, 31))
                .identificationNumber("ABCD123456")
                .categories(categoryEntities)
                .build();

        LocalDateTime currentDateTime = LocalDateTime.now();

        return DriverEntity.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .fatherName("Smith")
                .dateOfBirth(LocalDate.of(1997, 10, 1))
                .email("johnDoe@example.com")
                .address("Россия, Москва, 224001")
                .salary(35000)
                .createdAt(currentDateTime)
                .updatedAt(currentDateTime)
                .license(licenseEntity)
                .build();
    }

    private DriverDto createMockDriverDto(Long id) {
        Set<LicenseCategoryDto> categories = Set.of(LicenseCategoryDto.builder()
                .category(LicenseCategoryDto.CategoryType.CATEGORY_A).build());

        LicenseDto licenseDto = LicenseDto.builder()
                .issueDate(LocalDate.of(2022, 1, 1))
                .expirationDate(LocalDate.of(2025, 12, 31))
                .identificationNumber("ABCD123456")
                .categories(categories)
                .build();

        LocalDateTime currentDateTime = LocalDateTime.now();

        return DriverDto.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .fatherName("Smith")
                .dateOfBirth(LocalDate.of(1997, 10, 1))
                .email("johnDoe@example.com")
                .address("Россия, Москва, 224001")
                .salary(35000)
                .createdAt(currentDateTime)
                .updatedAt(currentDateTime)
                .license(licenseDto)
                .build();
    }

    private LicenseEntity createMockLicenseEntity() {
        Set<LicenseCategoryEntity> categoryEntities = new HashSet<>();
        LicenseCategoryEntity categoryEntity = LicenseCategoryEntity.builder()
                .category(LicenseCategoryEntity.CategoryType.CATEGORY_A)
                .build();
        categoryEntities.add(categoryEntity);

        return LicenseEntity.builder()
                .id(1L)
                .issueDate(LocalDate.of(2022, 1, 1))
                .expirationDate(LocalDate.of(2025, 12, 31))
                .identificationNumber("ABCD123456")
                .categories(categoryEntities)
                .build();
    }

    private LicenseCategoryEntity createMockLicenseCategoryEntity() {
        return LicenseCategoryEntity.builder()
                .id(1L)
                .category(LicenseCategoryEntity.CategoryType.CATEGORY_A)
                .build();
    }
}
