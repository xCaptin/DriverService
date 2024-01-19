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
//@AllArgsConstructor
@EqualsAndHashCode(exclude = "age")
public class DriverEntity {

    public DriverEntity(Long id, String firstName, String lastName,
                        String fatherName, LocalDate dateOfBirth,
                        String email, String address, Integer salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.address = address;
        this.salary = salary;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "firstName", nullable = false, length = 20)
    @NotNull(message = "First Name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 20)
    @NotNull(message = "Last name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String lastName;

    @Column(name = "fatherName", nullable = true, length = 20)
    @Pattern(regexp = "^[^0-9]+$")
    private String fatherName;

    @Column(name = "dateOfBirth", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    // Ex: 03-07-1984
    private LocalDate dateOfBirth;

    @Column(name = "age", nullable = false, insertable = false,
            // генерирует и сохраняет в таблице значение, равное разнице между текущим годом и годом рождения
            columnDefinition = "integer GENERATED ALWAYS AS (EXTRACT(YEAR FROM AGE(CURRENT_DATE, dateOfBirth))) " +
                    "STORED CHECK (age >= 18 AND age <= 65)")
//    @Setter(AccessLevel.NONE) // Исключаем генерацию сеттера для поля age
    @Getter
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
    @DecimalMin("19242")
    // Ex: 24000
    private Integer salary;

}
