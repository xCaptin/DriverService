package ru.driverservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.driverservice.dto.DriverDto;
import ru.driverservice.entity.DriverEntity;


@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverMapper INSTANCE = Mappers.getMapper(DriverMapper.class);

//    @Mapping(target = "age", expression = "java(driverEntity.calculateAge())")
//    @Mapping(target = "fatherName", ignore = true)
//    @Mapping(target = "dateOfBirth", ignore = true)
//    @Mapping(target = "email", ignore = true)
//    @Mapping(target = "address", ignore = true)
    DriverDto driverToDriverDto(DriverEntity driver);

//    @Mapping(target = "age", expression = "java(driverEntity.calculateAge())")
//    @Mapping(target = "fatherName", ignore = true)
//    @Mapping(target = "dateOfBirth", ignore = true)
//    @Mapping(target = "email", ignore = true)
//    @Mapping(target = "address", ignore = true)
    DriverEntity driverDtoToDriver(DriverDto driverDto);

    void updateDriverFromDto(DriverDto driverDto, @MappingTarget DriverEntity driverEntity);
}

//    Преобразование Driver в DriverDto
//    DriverDto driverDto = DriverMapper.INSTANCE.driverToDriverDto(driverEntity);

