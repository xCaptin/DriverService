package ru.driverservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "license")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate issueDate;

    @Column(name = "expiration_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;

    @Column(name = "identification_number", nullable = false, unique = true)
    @Size(min = 10, max = 15, message = "Identification number should be 10 to 15 characters")
    private String identificationNumber;

    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LicenseCategoryEntity> categories = new HashSet<>();

    @Override
    public String toString() {
        return "LicenseEntity{" +
                "id=" + id +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", identificationNumber='" + identificationNumber + '\'' +
                ", categories=" + categories +
                '}';
    }
}
