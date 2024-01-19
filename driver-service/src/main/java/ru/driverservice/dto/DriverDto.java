package ru.driverservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;


import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DriverDto {

    public DriverDto(String firstName, String lastName,
                     String fatherName, LocalDate dateOfBirth,
                     String email, String address, Integer salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.address = address;
        this.salary = salary;
    }

    @NotNull(message = "First Name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Pattern(regexp = "^[^0-9]+$")
    private String lastName;

    @Pattern(regexp = "^[^0-9]+$")
    private String fatherName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    // Ex: 03-07-1984
    private LocalDate dateOfBirth;

    @Setter(AccessLevel.NONE) // Исключаем генерацию сеттера для поля age
    @Getter
    private Integer age;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    // Ex: VasyaTaxi@yandex.ru
    private String email;

    @NotNull(message = "Address cannot be empty")
    @NotBlank(message = "The address cannot consist only of spaces")
    @Pattern(regexp = "[А-Я]\\w+, [А-Я]\\w+, \\d{6}")
    // Страна, Город, индекс (6 цифр)
    // Ex: Россия, Москва, 224001
    private String address;

    @NotNull(message = "Salary cannot be empty")
    @Positive
    @DecimalMin("19242")
    // Ex: 24000
    private Integer salary;
}