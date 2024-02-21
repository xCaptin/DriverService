package ru.driverservice.dto;

import lombok.Builder;

@Builder
public record LicenseCategoryDto(
        CategoryType category
) {
    public enum CategoryType {
        CATEGORY_A,
        CATEGORY_A1,
        CATEGORY_B,
        CATEGORY_BE,
        CATEGORY_C,
        CATEGORY_CE,
        CATEGORY_C1,
        CATEGORY_D,
        CATEGORY_D1,
        CATEGORY_M
    }
}
