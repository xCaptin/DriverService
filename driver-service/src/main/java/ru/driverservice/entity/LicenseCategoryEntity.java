package ru.driverservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Table(name = "license_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CategoryType category;

    @ManyToOne
    @JoinColumn(name = "license_id")
    private LicenseEntity license;

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
        CATEGORY_M,
    }

    @Override
    public String toString() {
        return "LicenseCategoryEntity{" +
                "id=" + id +
                ", category=" + category +
                ", license=" + license +
                '}';
    }
}


