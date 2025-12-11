package com.renault.renault.service.impl;

import com.renault.renault.dto.vehicle.VehicleDTO;
import com.renault.renault.entity.Garage;
import com.renault.renault.entity.Vehicle;
import com.renault.renault.exception.BusinessConstraintViolationException;
import com.renault.renault.exception.ResourceNotFoundException;
import com.renault.renault.mapper.VehicleMapper;
import com.renault.renault.repository.GarageRepository;
import com.renault.renault.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VehicleServiceImpl Unit Tests")
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Garage testGarage;
    private Vehicle testVehicle;
    private VehicleDTO testVehicleDTO;

    @BeforeEach
    void setUp() {
        testGarage = Garage.builder()
                .id(1L)
                .name("Test Garage")
                .address("123 Test St")
                .telephone("+33123456789")
                .email("test@garage.com")
                .vehicleCount(0)
                .vehicles(new ArrayList<>())
                .build();

        testVehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .manufacturingYear(2023)
                .fuelType("Diesel")
                .garage(testGarage)
                .accessories(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();

        testVehicleDTO = new VehicleDTO(
                1L,
                "Renault",
                2023,
                "Diesel",
                "Clio",
                1L,
                new ArrayList<>(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Add vehicle to garage successfully")
    void testAddVehicle_Success() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(testGarage));
        when(vehicleMapper.toEntity(testVehicleDTO)).thenReturn(testVehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        when(vehicleMapper.toDto(testVehicle)).thenReturn(testVehicleDTO);

        VehicleDTO result = vehicleService.addVehicle(1L, testVehicleDTO);

        assertNotNull(result);
        assertEquals("Clio", result.model());
        assertEquals("Renault", result.brand());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(garageRepository, times(1)).save(testGarage);
    }

    @Test
    @DisplayName("Add vehicle - garage not found")
    void testAddVehicle_GarageNotFound() {
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.addVehicle(999L, testVehicleDTO));
    }

    @Test
    @DisplayName("Add vehicle throws exception when garage at capacity")
    void testAddVehicle_GarageAtCapacity() {
        testGarage.setVehicleCount(50);
        when(garageRepository.findById(1L)).thenReturn(Optional.of(testGarage));

        assertThrows(BusinessConstraintViolationException.class, () -> vehicleService.addVehicle(1L, testVehicleDTO));
    }

    @Test
    @DisplayName("Update vehicle successfully")
    void testUpdateVehicle_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);
        when(vehicleMapper.toDto(testVehicle)).thenReturn(testVehicleDTO);

        VehicleDTO result = vehicleService.updateVehicle(1L, testVehicleDTO);

        assertNotNull(result);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Update vehicle - not found")
    void testUpdateVehicle_NotFound() {
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.updateVehicle(999L, testVehicleDTO));
    }

    @Test
    @DisplayName("Get vehicles by garage")
    void testGetVehiclesByGarage_Success() {
        List<Vehicle> vehicles = Arrays.asList(testVehicle);
        when(vehicleRepository.findByGarage_Id(1L)).thenReturn(vehicles);
        when(vehicleMapper.toDto(testVehicle)).thenReturn(testVehicleDTO);

        List<VehicleDTO> result = vehicleService.getVehiclesByGarage(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(vehicleRepository, times(1)).findByGarage_Id(1L);
    }

    @Test
    @DisplayName("Get vehicles by model")
    void testGetVehiclesByModel_Success() {
        List<Vehicle> vehicles = Arrays.asList(testVehicle);
        when(vehicleRepository.findByModel("Clio")).thenReturn(vehicles);
        when(vehicleMapper.toDto(testVehicle)).thenReturn(testVehicleDTO);

        List<VehicleDTO> result = vehicleService.getVehiclesByModel("Clio");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(vehicleRepository, times(1)).findByModel("Clio");
    }
}
