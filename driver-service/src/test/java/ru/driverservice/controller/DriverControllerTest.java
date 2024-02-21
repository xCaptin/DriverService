package ru.driverservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import ru.driverservice.dto.DriverDto;
import ru.driverservice.dto.LicenseCategoryDto;
import ru.driverservice.dto.LicenseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    void testCreateDriver() throws Exception {
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getDefaultDriver())))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetDriverById() throws Exception {
        DriverDto driverDto = getDefaultDriver();
        createDriver(driverDto);

        mockMvc.perform(get("/drivers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(driverDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(driverDto.lastName()))
                .andExpect(jsonPath("$.dateOfBirth").value(driverDto.dateOfBirth().format(formatter)))
                .andExpect(jsonPath("$.email").value(driverDto.email()))
                .andExpect(jsonPath("$.address").value(driverDto.address()))
                .andExpect(jsonPath("$.salary").value(driverDto.salary()))
                .andExpect(jsonPath("$.license.issueDate")
                        .value(driverDto.license().issueDate().format(formatter)))
                .andExpect(jsonPath("$.license.expirationDate")
                        .value(driverDto.license().expirationDate().format(formatter)))
                .andExpect(jsonPath("$.license.identificationNumber")
                        .value(driverDto.license().identificationNumber()))
                .andExpect(jsonPath("$.license.categories").isArray());
    }


    @Test
    void testUpdateDriver() throws Exception {
        DriverDto driverDto = getDefaultDriver();
        createDriver(driverDto);

        Set<LicenseCategoryDto> categories = Set.of(LicenseCategoryDto.builder()
                .category(LicenseCategoryDto.CategoryType.valueOf("CATEGORY_A")).build());

        LicenseDto updateLicenseDto = LicenseDto.builder()
                .issueDate(LocalDate.of(2023, 2, 1))
                .expirationDate(LocalDate.of(2026, 10, 17))
                .identificationNumber("ABCD123455")
                .categories(categories)
                .build();

        DriverDto updatedDriver = driverDto.withUpdatedValues(
                1L, "UpdatedJohn", "UpdatedDoe",
                "johnDoeUpdate@example.com", "Россия, Москва, 224002", updateLicenseDto);


        mockMvc.perform(put("/drivers/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDriver)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedDriver.firstName()))
                .andExpect(jsonPath("$.lastName").value(updatedDriver.lastName()))
                .andExpect(jsonPath("$.email").value(updatedDriver.email()))
                .andExpect(jsonPath("$.address").value(updatedDriver.address()))
                .andExpect(jsonPath("$.license.issueDate").value(updatedDriver.license().issueDate().format(formatter)))
                .andExpect(jsonPath("$.license.expirationDate").value(updatedDriver.license().expirationDate().format(formatter)))
                .andExpect(jsonPath("$.license.identificationNumber").value(updatedDriver.license().identificationNumber()))
                .andExpect(jsonPath("$.license.categories[0].category").value("CATEGORY_A"));

    }

    @Test
    public void testDeleteDriver() throws Exception {
        createDriver(getDefaultDriver());
        mockMvc.perform(delete("/drivers/{id}", 1))
                .andExpect(status().isOk());
        mockMvc.perform(get("/drivers/{id}", 1))
                .andExpect(status().isNotFound());
    }

    private DriverDto getDefaultDriver() {

        Set<LicenseCategoryDto> categories = Set.of(LicenseCategoryDto.builder()
                .category(LicenseCategoryDto.CategoryType.valueOf("CATEGORY_A")).build());


        LicenseDto licenseDto = LicenseDto.builder()
                .issueDate(LocalDate.of(2022, 1, 1))
                .expirationDate(LocalDate.of(2025, 12, 31))
                .identificationNumber("ABCD123456")
                .categories(categories)
                .build();


        LocalDateTime currentDateTime = LocalDateTime.now();

        return DriverDto.builder()
                .firstName("John")
                .lastName("Doe")
                .fatherName("Smith")
                .dateOfBirth(LocalDate.of(1997, 10, 1))
                .email("johnDoe@example.com")
                .address("Россия, Москва, 224001")
                .salary(35000)
                .createdAt(currentDateTime)
                .updatedAt(currentDateTime)
                .license(licenseDto)
                .build();
    }


    private void createDriver(DriverDto driverDto) throws Exception {
        mockMvc.perform(post("/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(driverDto)));
    }
}
