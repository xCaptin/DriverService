package ru.driverservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GenerationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_Name", nullable = false, length = 20)
    @NotNull(message = "First Name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String firstName;

    @Column(name = "last_Name", nullable = false, length = 20)
    @NotNull(message = "Last name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String lastName;

    @Column(name = "father_Name", length = 20)
    @Pattern(regexp = "^[^0-9]+$")
    private String fatherName;

    @Column(name = "date_Of_Birth", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "address", nullable = false, unique = true, length = 100)
    @NotNull(message = "Address cannot be empty")
    @NotBlank(message = "The address cannot consist only of spaces")
    @Pattern(regexp = "[А-Я]\\w+, [А-Я]\\w+, \\d{6}")
    private String address;

    @Column(name = "salary", nullable = false)
    @NotNull(message = "Salary cannot be empty")
    @Positive
    private Integer salary;

    @CreatedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "license_id", referencedColumnName = "id")
    private LicenseEntity license;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverEntity that = (DriverEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(fatherName, that.fatherName) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(age, that.age) && Objects.equals(email, that.email) && Objects.equals(address, that.address) && Objects.equals(salary, that.salary) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(license, that.license);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, fatherName, dateOfBirth, age, email, address, salary, createdAt, updatedAt, license);
    }

    @Override
    public String toString() {
        return "DriverEntity{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + salary +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", license=" + license +
                '}';
    }
}
