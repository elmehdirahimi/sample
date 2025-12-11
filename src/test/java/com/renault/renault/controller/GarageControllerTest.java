package com.renault.renault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.renault.dto.common.GarageSearchCriteria;
import com.renault.renault.dto.common.OpeningTimeDTO;
import com.renault.renault.dto.garage.GarageDTO;
import com.renault.renault.exception.ResourceNotFoundException;
import com.renault.renault.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GarageController.class)
@DisplayName("GarageController Integration Tests")
class GarageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GarageService garageService;

    private GarageDTO testGarageDTO;
    private Set<OpeningTimeDTO> openingTimes;

    @BeforeEach
    void setUp() {
        openingTimes = new HashSet<>();
        openingTimes.add(new OpeningTimeDTO(1L, LocalTime.of(9, 0), LocalTime.of(18, 0), DayOfWeek.MONDAY));
        openingTimes.add(new OpeningTimeDTO(2L, LocalTime.of(9, 0), LocalTime.of(18, 0), DayOfWeek.TUESDAY));

        testGarageDTO = new GarageDTO(
                1L,
                "Garage Paris Centre",
                "123 Avenue des Champs-Élysées, 75008 Paris",
                "+33123456789",
                "contact@garage-paris.com",
                10,
                50,
                openingTimes
        );
    }

    @Test
    @DisplayName("POST /api/garages - Create garage successfully")
    void createGarage_Success() throws Exception {
        when(garageService.createGarage(any(GarageDTO.class))).thenReturn(testGarageDTO);

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGarageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Garage Paris Centre"))
                .andExpect(jsonPath("$.email").value("contact@garage-paris.com"))
                .andExpect(jsonPath("$.vehicleCount").value(10))
                .andExpect(jsonPath("$.maxVehicles").value(50));

        verify(garageService, times(1)).createGarage(any(GarageDTO.class));
    }

    @Test
    @DisplayName("POST /api/garages - Create garage with invalid data returns 400")
    void createGarage_InvalidData_Returns400() throws Exception {
        GarageDTO invalidGarage = new GarageDTO(
                null,
                "", // Empty name - should fail validation
                "Address",
                "+33123456789",
                "invalid-email", // Invalid email format
                0,
                50,
                openingTimes
        );

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidGarage)))
                .andExpect(status().isBadRequest());

        verify(garageService, never()).createGarage(any(GarageDTO.class));
    }

    @Test
    @DisplayName("GET /api/garages/{id} - Get garage by ID successfully")
    void getGarageById_Success() throws Exception {
        when(garageService.getGarageById(1L)).thenReturn(testGarageDTO);

        mockMvc.perform(get("/api/garages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Garage Paris Centre"))
                .andExpect(jsonPath("$.address").value("123 Avenue des Champs-Élysées, 75008 Paris"));

        verify(garageService, times(1)).getGarageById(1L);
    }

    @Test
    @DisplayName("GET /api/garages/{id} - Get garage by ID not found returns 404")
    void getGarageById_NotFound_Returns404() throws Exception {
        when(garageService.getGarageById(999L))
                .thenThrow(new ResourceNotFoundException("Garage not found with ID: 999"));

        mockMvc.perform(get("/api/garages/999"))
                .andExpect(status().isNotFound());

        verify(garageService, times(1)).getGarageById(999L);
    }

    @Test
    @DisplayName("PUT /api/garages/{id} - Update garage successfully")
    void updateGarage_Success() throws Exception {
        GarageDTO updatedGarage = new GarageDTO(
                1L,
                "Garage Paris Updated",
                "456 New Address",
                "+33987654321",
                "updated@garage.com",
                15,
                50,
                openingTimes
        );

        when(garageService.updateGarage(eq(1L), any(GarageDTO.class))).thenReturn(updatedGarage);

        mockMvc.perform(put("/api/garages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGarage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage Paris Updated"))
                .andExpect(jsonPath("$.address").value("456 New Address"))
                .andExpect(jsonPath("$.email").value("updated@garage.com"));

        verify(garageService, times(1)).updateGarage(eq(1L), any(GarageDTO.class));
    }

    @Test
    @DisplayName("PUT /api/garages/{id} - Update non-existent garage returns 404")
    void updateGarage_NotFound_Returns404() throws Exception {
        when(garageService.updateGarage(eq(999L), any(GarageDTO.class)))
                .thenThrow(new ResourceNotFoundException("Garage not found with ID: 999"));

        mockMvc.perform(put("/api/garages/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testGarageDTO)))
                .andExpect(status().isNotFound());

        verify(garageService, times(1)).updateGarage(eq(999L), any(GarageDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/garages/{id} - Delete garage successfully")
    void deleteGarage_Success() throws Exception {
        doNothing().when(garageService).deleteGarage(1L);

        mockMvc.perform(delete("/api/garages/1"))
                .andExpect(status().isNoContent());

        verify(garageService, times(1)).deleteGarage(1L);
    }

    @Test
    @DisplayName("GET /api/garages - Get all garages with pagination")
    void getAllGarages_Success() throws Exception {
        List<GarageDTO> garages = List.of(testGarageDTO);
        Page<GarageDTO> page = new PageImpl<>(garages, PageRequest.of(0, 10), 1);

        when(garageService.getAllGarages(any(), anyString())).thenReturn(page);

        mockMvc.perform(get("/api/garages")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Garage Paris Centre"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(garageService, times(1)).getAllGarages(any(), anyString());
    }

    @Test
    @DisplayName("GET /api/garages/search - Search garages by name")
    void searchGarages_ByName_Success() throws Exception {
        List<GarageDTO> results = List.of(testGarageDTO);
        when(garageService.searchGarages(any(GarageSearchCriteria.class))).thenReturn(results);

        mockMvc.perform(get("/api/garages/search")
                        .param("name", "Paris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Garage Paris Centre"));

        verify(garageService, times(1)).searchGarages(any(GarageSearchCriteria.class));
    }

    @Test
    @DisplayName("GET /api/garages/search - Search garages by model")
    void searchGarages_ByModel_Success() throws Exception {
        List<GarageDTO> results = List.of(testGarageDTO);
        when(garageService.searchGarages(any(GarageSearchCriteria.class))).thenReturn(results);

        mockMvc.perform(get("/api/garages/search")
                        .param("model", "Clio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Garage Paris Centre"));

        verify(garageService, times(1)).searchGarages(any(GarageSearchCriteria.class));
    }

    @Test
    @DisplayName("GET /api/garages/search - Search garages by fuel type")
    void searchGarages_ByFuelType_Success() throws Exception {
        List<GarageDTO> results = List.of(testGarageDTO);
        when(garageService.searchGarages(any(GarageSearchCriteria.class))).thenReturn(results);

        mockMvc.perform(get("/api/garages/search")
                        .param("fuelType", "DIESEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(garageService, times(1)).searchGarages(any(GarageSearchCriteria.class));
    }

    @Test
    @DisplayName("GET /api/garages/search - Search garages by accessory")
    void searchGarages_ByAccessory_Success() throws Exception {
        List<GarageDTO> results = List.of(testGarageDTO);
        when(garageService.searchGarages(any(GarageSearchCriteria.class))).thenReturn(results);

        mockMvc.perform(get("/api/garages/search")
                        .param("accessory", "GPS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(garageService, times(1)).searchGarages(any(GarageSearchCriteria.class));
    }

    @Test
    @DisplayName("GET /api/garages/search - Search without criteria returns 400")
    void searchGarages_NoCriteria_Returns400() throws Exception {
        mockMvc.perform(get("/api/garages/search"))
                .andExpect(status().isBadRequest());

        verify(garageService, never()).searchGarages(any(GarageSearchCriteria.class));
    }

    @Test
    @DisplayName("GET /api/garages/search - Search with no results returns 204")
    void searchGarages_NoResults_Returns204() throws Exception {
        when(garageService.searchGarages(any(GarageSearchCriteria.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/garages/search")
                        .param("name", "NonExistent"))
                .andExpect(status().isNoContent());

        verify(garageService, times(1)).searchGarages(any(GarageSearchCriteria.class));
    }

    @Test
    @DisplayName("GET /api/garages/search - Search with multiple criteria")
    void searchGarages_MultipleCriteria_Success() throws Exception {
        List<GarageDTO> results = List.of(testGarageDTO);
        when(garageService.searchGarages(any(GarageSearchCriteria.class))).thenReturn(results);

        mockMvc.perform(get("/api/garages/search")
                        .param("name", "Paris")
                        .param("model", "Clio")
                        .param("fuelType", "DIESEL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(garageService, times(1)).searchGarages(any(GarageSearchCriteria.class));
    }
}
