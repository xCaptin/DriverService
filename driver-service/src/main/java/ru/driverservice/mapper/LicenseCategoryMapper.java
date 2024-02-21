package ru.driverservice.mapper;

import org.mapstruct.Mapper;
import ru.driverservice.dto.LicenseCategoryDto;
import ru.driverservice.entity.LicenseCategoryEntity;

@Mapper(componentModel = "spring")
public interface LicenseCategoryMapper {
    LicenseCategoryDto licenseCategoryToLicenseCategoryDto(LicenseCategoryEntity licenseCategory);

    LicenseCategoryEntity licenseCategoryDtoToLicenseCategory(LicenseCategoryDto licenseCategoryDto);

}
