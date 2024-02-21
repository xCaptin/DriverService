package ru.driverservice.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ru.driverservice.dto.DriverDto;
import ru.driverservice.entity.DriverEntity;
import ru.driverservice.mapper.DriverMapper;
import ru.driverservice.service.DriverService;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
@RequestMapping("/drivers")
public class DriverController {

    DriverService driverService;
    DriverMapper driverMapper;

    @GetMapping("/{id}")
    public DriverDto getDriverById(@PathVariable Long id) {
        return driverMapper.driverToDriverDto(driverService.getDriverById(id));
    }

    @PostMapping
    public DriverDto createDriver(@Valid @RequestBody DriverDto driverDto) {
        return driverMapper.driverToDriverDto(driverService.createDriver(driverDto));
    }

    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id, @Valid @RequestBody DriverDto driverDto) {
        return driverMapper.driverToDriverDto(driverService.updateDriver(id, driverDto));
    }

    @DeleteMapping("/{id}")
    public DriverDto deleteDriver(@PathVariable Long id) {
        return driverMapper.driverToDriverDto(driverService.deleteDriver(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DriverDto>> getDriversByFilter(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Pageable pageable
    ) {
        Page<DriverEntity> drivers = driverService.getDriversByFilter(firstName, lastName, minSalary, maxSalary, minAge, maxAge, pageable);
        Page<DriverDto> driverDtos = drivers.map(driverMapper::driverToDriverDto);
        return ResponseEntity.ok(driverDtos);
    }
}
