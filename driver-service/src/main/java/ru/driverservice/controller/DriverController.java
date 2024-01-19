package ru.driverservice.controller;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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
        return driverMapper.INSTANCE.driverToDriverDto(driverService.getDriverById(id));
    }

    @PostMapping
    public DriverDto createDriver(@Valid @RequestBody DriverDto driverDto) {
        return driverMapper.INSTANCE.driverToDriverDto(driverService.createDriver(driverDto));
    }

    @PutMapping("/{id}")
    public DriverDto updateDriver(@PathVariable Long id, @Valid @RequestBody DriverDto driverDto) {
        return driverMapper.INSTANCE.driverToDriverDto(driverService.updateDriver(id, driverDto));
    }

    @DeleteMapping("/{id}")
    public DriverDto deleteDriver(@PathVariable Long id) {
        return driverMapper.INSTANCE.driverToDriverDto(driverService.deleteDriver(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DriverEntity>> getDriversByFilter(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable
    ) {
        Page<DriverEntity> drivers = driverService.getDriversByFilter(firstName, lastName, pageable);
        return ResponseEntity.ok(drivers);
    }
}
