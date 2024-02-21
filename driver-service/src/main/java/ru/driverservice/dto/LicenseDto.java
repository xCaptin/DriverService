package ru.driverservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record LicenseDto(
        @NotNull(message = "Issue date cannot be null") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate issueDate,
        @NotNull(message = "Expiration date cannot be null") @JsonFormat(pattern = "dd-MM-yyyy") LocalDate expirationDate,
        @NotEmpty(message = "Identification number cannot be empty")
        @Size(min = 10, max = 15, message = "Identification number should be 10 to 15 characters") String identificationNumber,
        Set<LicenseCategoryDto> categories
) {

    public LicenseDto withCategories(Set<LicenseCategoryDto> categories) {
        return new LicenseDto(issueDate, expirationDate, identificationNumber, categories);
    }

}
