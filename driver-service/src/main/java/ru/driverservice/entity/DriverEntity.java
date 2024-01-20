package ru.driverservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_id_seq")
    @SequenceGenerator(name = "driver_id_seq", sequenceName = "driver_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "firstName", nullable = false, length = 20)
    @NotNull(message = "First Name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 20)
    @NotNull(message = "Last name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String lastName;

    @Column(name = "fatherName", length = 20)
    @Pattern(regexp = "^[^0-9]+$")
    private String fatherName;

    @Column(name = "dateOfBirth", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    // Ex: 03-07-1984
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    // Ex: VasyaTaxi@yandex.ru
    private String email;

    @Column(name = "address", nullable = false, unique = true, length = 100)
    @NotNull(message = "Address cannot be empty")
    @NotBlank(message = "The address cannot consist only of spaces")
    @Pattern(regexp = "[А-Я]\\w+, [А-Я]\\w+, \\d{6}")
    // Страна, Город, индекс (6 цифр)
    // Ex: Россия, Москва, 224001
    private String address;

    @Column(name = "salary", nullable = false)
    @NotNull(message = "Salary cannot be empty")
    @Positive
    // Ex: 24000
    private Integer salary;

}
