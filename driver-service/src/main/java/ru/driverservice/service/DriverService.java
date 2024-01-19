package ru.driverservice.service;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.driverservice.dto.DriverDto;
import ru.driverservice.entity.DriverEntity;
import ru.driverservice.exeption.NotFoundException;
import ru.driverservice.mapper.DriverMapper;
import ru.driverservice.repository.DriverRepository;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DriverService {

    DriverRepository driverRepository;
    DriverMapper driverMapper;

    public DriverEntity getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found"));
    }

    public DriverEntity createDriver(@Valid DriverDto driverDto) {
        DriverEntity driverEntity = DriverMapper.INSTANCE.driverDtoToDriver(driverDto);
        return driverRepository.save(driverEntity);
    }

    public DriverEntity updateDriver(Long id, @Valid DriverDto driverDto) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        driverMapper.updateDriverFromDto(driverDto, driverEntity);

        return driverRepository.save(driverEntity);
    }

    public DriverEntity deleteDriver(Long id) {
        DriverEntity driverEntity = driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found"));

        driverRepository.deleteById(id);

        return driverEntity;
    }

    public Page<DriverEntity> getDriversByFilter(String firstName, String lastName, Pageable pageable) {
        if (firstName == null && lastName == null) {
            return driverRepository.findAll(pageable);
        } else if (firstName == null) {
            return driverRepository.findAllByLastNameContaining(lastName, pageable);
        } else if (lastName == null) {
            return driverRepository.findAllByFirstNameContaining(firstName, pageable);
        } else {
            return driverRepository.findAllByFirstNameAndLastNameContaining(firstName, lastName, pageable);
        }
    }

}
