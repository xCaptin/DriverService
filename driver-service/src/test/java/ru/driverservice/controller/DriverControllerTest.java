package ru.driverservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.driverservice.dto.DriverDto;
import ru.driverservice.entity.DriverEntity;
import ru.driverservice.mapper.DriverMapper;
import ru.driverservice.service.DriverService;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    @MockBean
    private DriverMapper driverMapper;

    private Long driverId;
    private String firstName;
    private String lastName;
    private String fatherName;
    private LocalDate dateOfBirth;
    private DateTimeFormatter formatter;
    private String formattedDateOfBirth;
    private String email;
    private String address;
    private Integer salary;

    @BeforeEach
    public void setUp() {
        driverId = 1L;
        firstName = "John";
        lastName = "Doe";
        fatherName = "Smith";
        dateOfBirth = LocalDate.of(1997, 10, 1);
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        formattedDateOfBirth = dateOfBirth.format(formatter);
        email = "johnDoe@example.com";
        address = "Россия, Москва, 224001";
        salary = 35000;
    }

    @Test
    public void testGetDriverById() throws Exception {

        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setId(driverId);
        driverEntity.setFirstName(firstName);
        driverEntity.setLastName(lastName);
        driverEntity.setFatherName(fatherName);
        driverEntity.setDateOfBirth(LocalDate.parse(formattedDateOfBirth, formatter));
        driverEntity.setEmail(email);
        driverEntity.setAddress(address);
        driverEntity.setSalary(salary);

        DriverDto driverDto = DriverMapper.INSTANCE.driverToDriverDto(driverEntity);

        when(driverService.getDriverById(driverId)).thenReturn(driverEntity);
        when(driverMapper.driverToDriverDto(driverEntity)).thenReturn(driverDto);

        mockMvc.perform(get("/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.dateOfBirth").value(formattedDateOfBirth))
                .andExpect(jsonPath("$.email").value(driverEntity.getEmail()))
                .andExpect(jsonPath("$.address").value(driverEntity.getAddress()))
                .andExpect(jsonPath("$.salary").value(driverEntity.getSalary()));
    }

    @Test
    public void testCreateDriver() throws Exception {
        DriverDto driverDto = new DriverDto();
        driverDto.setFirstName(firstName);
        driverDto.setLastName(lastName);


        DriverEntity createdDriver = new DriverEntity();
        createdDriver.setFirstName(firstName);
        createdDriver.setLastName(lastName);

        when(driverService.createDriver(any(DriverDto.class))).thenReturn(createdDriver);

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @Test
    public void testUpdateDriver() throws Exception {
        Long driverId = 1L;
        String updatedFirstName = "UpdatedJohn";
        String updatedLastName = "UpdatedDoe";

        DriverDto updatedDriverDto = new DriverDto();
        updatedDriverDto.setFirstName(updatedFirstName);
        updatedDriverDto.setLastName(updatedLastName);

        DriverEntity updatedDriverEntity = new DriverEntity();
        updatedDriverEntity.setFirstName(updatedFirstName);
        updatedDriverEntity.setLastName(updatedLastName);

        when(driverService.updateDriver(driverId, updatedDriverDto)).thenReturn(updatedDriverEntity);
        when(driverMapper.driverToDriverDto(updatedDriverEntity)).thenReturn(updatedDriverDto);

        mockMvc.perform(put("/drivers/{id}", driverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDriverDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updatedFirstName))
                .andExpect(jsonPath("$.lastName").value(updatedLastName));
    }

    @Test
    public void testDeleteDriver() throws Exception {
        DriverEntity deletedDriverEntity = new DriverEntity();
        deletedDriverEntity.setFirstName(firstName);
        deletedDriverEntity.setLastName(lastName);

        DriverDto deletedDriverDto = new DriverDto();
        deletedDriverDto.setFirstName(firstName);
        deletedDriverDto.setLastName(lastName);

        when(driverService.deleteDriver(driverId)).thenReturn(deletedDriverEntity);
        when(driverMapper.driverToDriverDto(deletedDriverEntity)).thenReturn(deletedDriverDto);

        mockMvc.perform(delete("/drivers/{id}", driverId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    @Test
    public void testGetDriversByFilter() throws Exception {
        String firstName = "John";
        String lastName = "Doe";

        DriverEntity driverEntity1 = new DriverEntity();
        driverEntity1.setFirstName(firstName);
        driverEntity1.setLastName(lastName);

        DriverEntity driverEntity2 = new DriverEntity();
        driverEntity2.setFirstName(firstName + "First");
        driverEntity2.setLastName(lastName + "First");

        List<DriverEntity> driverEntities = List.of(driverEntity1, driverEntity2);
        Page<DriverEntity> driversPage = new PageImpl<>(driverEntities);

        when(driverService.getDriversByFilter(firstName, lastName, Pageable.unpaged()))
                .thenReturn(driversPage);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(new DriverFilterDto(firstName, lastName));

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value(firstName))
                .andExpect(jsonPath("$.content[0].lastName").value(lastName))
                .andExpect(jsonPath("$.content[1].firstName").value(firstName + "First"))
                .andExpect(jsonPath("$.content[1].lastName").value(lastName + "First"));
    }
}
