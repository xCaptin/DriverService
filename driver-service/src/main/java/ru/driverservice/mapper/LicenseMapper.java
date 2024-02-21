package ru.driverservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.driverservice.dto.LicenseDto;
import ru.driverservice.entity.LicenseEntity;

@Mapper(componentModel = "spring")
public interface LicenseMapper {
    LicenseDto licenseToLicenseDto(LicenseEntity license);

    LicenseEntity licenseDtoToLicense(LicenseDto licenseDto);

    void updateLicenseFromDto(LicenseDto licenseDto, @MappingTarget LicenseEntity licenseEntity);
}
