package ru.driverservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DriverDto(

        Long id,
        @NotNull(message = "First Name cannot be empty")
        @Pattern(regexp = "^[^0-9]+$")
        String firstName,

        @NotNull(message = "Last name cannot be empty")
        @Pattern(regexp = "^[^0-9]+$")
        String lastName,

        @Pattern(regexp = "^[^0-9]+$")
        String fatherName,

        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dateOfBirth,

        Integer age,

        @NotNull(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Address cannot be empty")
        @NotBlank(message = "The address cannot consist only of spaces")
        @Pattern(regexp = "[А-Я]\\w+, [А-Я]\\w+, \\d{6}")
        String address,

        @NotNull(message = "Salary cannot be empty")
        @Positive
        @DecimalMin("19242")
        Integer salary,

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime updatedAt,

        LicenseDto license
) {

    public DriverDto withLicense(LicenseDto license) {
        return new DriverDto(id, firstName, lastName, fatherName, dateOfBirth, age, email, address, salary, createdAt, updatedAt, license);
    }

    public DriverDto withUpdatedValues
            (Long id, String updatedFirstName, String updatedLastName, String updateEmail, String updateAddress, LicenseDto updateLicense) {

        return new DriverDto(id, updatedFirstName, updatedLastName, fatherName, dateOfBirth, age,
                             updateEmail, updateAddress, salary, createdAt, updatedAt, updateLicense);
    }
}
