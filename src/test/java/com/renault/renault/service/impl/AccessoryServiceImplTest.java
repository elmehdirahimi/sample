package com.renault.renault.service.impl;

import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.entity.Accessory;
import com.renault.renault.entity.Vehicle;
import com.renault.renault.mapper.AccessoryMapper;
import com.renault.renault.repository.AccessoryRepository;
import com.renault.renault.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccessoryServiceImpl Unit Tests")
class AccessoryServiceImplTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoryService;

    private Vehicle testVehicle;
    private Accessory testAccessory;
    private AccessoryDTO testAccessoryDTO;

    @BeforeEach
    void setUp() {
        testVehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .manufacturingYear(2023)
                .fuelType("Diesel")
                .accessories(new ArrayList<>())
                .build();

        testAccessory = Accessory.builder()
                .id(1L)
                .name("GPS Navigation System")
                .description("Advanced GPS with real-time traffic")
                .price(299.99)
                .type("Navigation")
                .vehicle(testVehicle)
                .build();

        testAccessoryDTO = new AccessoryDTO(
                1L,
                "GPS Navigation System",
                "Advanced GPS with real-time traffic",
                299.99,
                "Navigation"
        );
    }

    @Test
    @DisplayName("Add accessory to vehicle successfully")
    void testAddAccessory_Success() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(accessoryMapper.toEntity(testAccessoryDTO)).thenReturn(testAccessory);
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(testAccessory);
        when(accessoryMapper.toDto(testAccessory)).thenReturn(testAccessoryDTO);

        // Act
        AccessoryDTO result = accessoryService.addAccessory(1L, testAccessoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals("GPS Navigation System", result.name());
        assertEquals(299.99, result.price());
        verify(accessoryRepository, times(1)).save(any(Accessory.class));
    }

    @Test
    @DisplayName("Add accessory throws exception when vehicle not found")
    void testAddAccessory_VehicleNotFound() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accessoryService.addAccessory(999L, testAccessoryDTO));
    }

    @Test
    @DisplayName("Update accessory successfully")
    void testUpdateAccessory_Success() {
        // Arrange
        AccessoryDTO updatedDTO = new AccessoryDTO(
                1L,
                "Updated GPS",
                "Updated description",
                349.99,
                "Navigation"
        );
        testAccessory.setName("Updated GPS");
        testAccessory.setPrice(349.99);

        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(testAccessory));
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(testAccessory);
        when(accessoryMapper.toDto(testAccessory)).thenReturn(updatedDTO);

        // Act
        AccessoryDTO result = accessoryService.updateAccessory(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated GPS", result.name());
        verify(accessoryRepository, times(1)).save(any(Accessory.class));
    }

    @Test
    @DisplayName("Update accessory throws exception when not found")
    void testUpdateAccessory_NotFound() {
        // Arrange
        when(accessoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accessoryService.updateAccessory(999L, testAccessoryDTO));
    }

    @Test
    @DisplayName("Get accessories by vehicle returns empty list")
    void testGetAccessoriesByVehicle_Empty() {
        // Arrange
        when(accessoryRepository.findByVehicle_Id(1L)).thenReturn(new ArrayList<>());

        // Act
        List<AccessoryDTO> result = accessoryService.getAccessoriesByVehicle(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(accessoryRepository, times(1)).findByVehicle_Id(1L);
    }
}
