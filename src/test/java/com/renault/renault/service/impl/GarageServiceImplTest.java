package com.renault.renault.service.impl;

import com.renault.renault.dto.garage.GarageDTO;
import com.renault.renault.entity.Garage;
import com.renault.renault.mapper.GarageMapper;
import com.renault.renault.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GarageServiceImpl Unit Tests")
class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    private Garage testGarage;
    private GarageDTO testGarageDTO;

    @BeforeEach
    void setUp() {
        testGarage = Garage.builder()
                .id(1L)
                .name("Garage Paris Centre")
                .address("123 Avenue des Champs-Élysées, 75008 Paris")
                .telephone("+33123456789")
                .email("contact@garage-paris.com")
                .vehicleCount(0)
                .build();

        testGarageDTO = new GarageDTO(
                1L,
                "Garage Paris Centre",
                "123 Avenue des Champs-Élysées, 75008 Paris",
                "+33123456789",
                "contact@garage-paris.com",
                0,
                50,
                Collections.emptySet()
        );
    }

    @Test
    @DisplayName("Create garage successfully")
    void testCreateGarage_Success() {
        // Arrange
        when(garageMapper.toEntity(testGarageDTO)).thenReturn(testGarage);
        when(garageRepository.save(testGarage)).thenReturn(testGarage);
        when(garageMapper.toDto(testGarage)).thenReturn(testGarageDTO);

        // Act
        GarageDTO result = garageService.createGarage(testGarageDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Garage Paris Centre", result.name());
        verify(garageRepository, times(1)).save(testGarage);
    }

    @Test
    @DisplayName("Get garage by ID successfully")
    void testGetGarageById_Success() {
        // Arrange
        when(garageRepository.findById(1L)).thenReturn(Optional.of(testGarage));
        when(garageMapper.toDto(testGarage)).thenReturn(testGarageDTO);

        // Act
        GarageDTO result = garageService.getGarageById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(garageRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get garage by ID throws exception when not found")
    void testGetGarageById_NotFound() {
        // Arrange
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> garageService.getGarageById(999L));
    }

    @Test
    @DisplayName("Get all garages with pagination")
    void testGetAllGarages_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Garage> garagePage = new PageImpl<>(Arrays.asList(testGarage), pageable, 1);
        when(garageRepository.findAll(pageable)).thenReturn(garagePage);
        when(garageMapper.toDto(testGarage)).thenReturn(testGarageDTO);

        // Act
        Page<GarageDTO> result = garageService.getAllGarages(pageable, "name");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(garageRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Update garage successfully")
    void testUpdateGarage_Success() {
        // Arrange
        GarageDTO updatedDTO = new GarageDTO(
                1L,
                "Garage Paris Updated",
                "123 Avenue des Champs-Élysées, 75008 Paris",
                "+33987654321",
                "updated@garage-paris.com",
                0,
                50,
                Collections.emptySet()
        );
        testGarage.setName("Garage Paris Updated");

        when(garageRepository.findById(1L)).thenReturn(Optional.of(testGarage));
        when(garageRepository.save(any(Garage.class))).thenReturn(testGarage);
        when(garageMapper.toDto(testGarage)).thenReturn(updatedDTO);

        // Act
        GarageDTO result = garageService.updateGarage(1L, updatedDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Garage Paris Updated", result.name());
        verify(garageRepository, times(1)).save(any(Garage.class));
    }

    @Test
    @DisplayName("Search garages by name")
    void testSearchGaragesByName_Success() {
        // Arrange
        List<Garage> garages = Arrays.asList(testGarage);
        when(garageRepository.findByNameContainingIgnoreCase("Paris")).thenReturn(garages);
        when(garageMapper.toDto(testGarage)).thenReturn(testGarageDTO);

        // Act
        List<GarageDTO> result = garageService.searchGaragesByName("Paris");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(garageRepository, times(1)).findByNameContainingIgnoreCase("Paris");
    }

    @Test
    @DisplayName("Search garages by vehicle model")
    void testSearchGaragesByVehicleModel_Success() {
        // Arrange
        List<Garage> garages = Arrays.asList(testGarage);
        when(garageRepository.findByVehicles_Model("Clio")).thenReturn(garages);
        when(garageMapper.toDto(testGarage)).thenReturn(testGarageDTO);

        // Act
        List<GarageDTO> result = garageService.searchGaragesByVehicleModel("Clio");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(garageRepository, times(1)).findByVehicles_Model("Clio");
    }
}
