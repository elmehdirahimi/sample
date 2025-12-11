package com.renault.renault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.dto.vehicle.VehicleDTO;
import com.renault.renault.exception.BusinessConstraintViolationException;
import com.renault.renault.exception.ResourceNotFoundException;
import com.renault.renault.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
@DisplayName("VehicleController Integration Tests")
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    private VehicleDTO testVehicleDTO;
    private List<AccessoryDTO> accessories;

    @BeforeEach
    void setUp() {
        accessories = new ArrayList<>();
        accessories.add(new AccessoryDTO(1L, "GPS Navigation", "Advanced GPS system", 299.99, "Navigation"));

        testVehicleDTO = new VehicleDTO(
                1L,
                "Renault",
                2023,
                "Diesel",
                "Clio",
                1L,
                accessories,
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /api/vehicles/garage/{garageId} - Add vehicle successfully")
    void addVehicle_Success() throws Exception {
        when(vehicleService.addVehicle(eq(1L), any(VehicleDTO.class))).thenReturn(testVehicleDTO);

        mockMvc.perform(post("/api/vehicles/garage/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVehicleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.model").value("Clio"))
                .andExpect(jsonPath("$.fuelType").value("Diesel"))
                .andExpect(jsonPath("$.manufacturingYear").value(2023))
                .andExpect(jsonPath("$.garageId").value(1));

        verify(vehicleService, times(1)).addVehicle(eq(1L), any(VehicleDTO.class));
    }

    @Test
    @DisplayName("POST /api/vehicles/garage/{garageId} - Add vehicle with invalid data returns 400")
    void addVehicle_InvalidData_Returns400() throws Exception {
        VehicleDTO invalidVehicle = new VehicleDTO(
                null,
                "", // Empty brand - should fail validation
                1800, // Year too old - should fail validation
                "Diesel",
                "Clio",
                1L,
                accessories,
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/vehicles/garage/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehicle)))
                .andExpect(status().isBadRequest());

        verify(vehicleService, never()).addVehicle(anyLong(), any(VehicleDTO.class));
    }

    @Test
    @DisplayName("POST /api/vehicles/garage/{garageId} - Add vehicle to non-existent garage returns 404")
    void addVehicle_GarageNotFound_Returns404() throws Exception {
        when(vehicleService.addVehicle(eq(999L), any(VehicleDTO.class)))
                .thenThrow(new ResourceNotFoundException("Garage not found with ID: 999"));

        mockMvc.perform(post("/api/vehicles/garage/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVehicleDTO)))
                .andExpect(status().isNotFound());

        verify(vehicleService, times(1)).addVehicle(eq(999L), any(VehicleDTO.class));
    }

    @Test
    @DisplayName("POST /api/vehicles/garage/{garageId} - Add vehicle to full garage returns 400")
    void addVehicle_GarageFull_Returns400() throws Exception {
        when(vehicleService.addVehicle(eq(1L), any(VehicleDTO.class)))
                .thenThrow(new BusinessConstraintViolationException("Garage has reached the maximum limit of 50 vehicles"));

        mockMvc.perform(post("/api/vehicles/garage/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVehicleDTO)))
                .andExpect(status().isBadRequest());

        verify(vehicleService, times(1)).addVehicle(eq(1L), any(VehicleDTO.class));
    }

    @Test
    @DisplayName("PUT /api/vehicles/{id} - Update vehicle successfully")
    void updateVehicle_Success() throws Exception {
        VehicleDTO updatedVehicle = new VehicleDTO(
                1L,
                "Renault",
                2024,
                "Electric",
                "Zoe",
                1L,
                accessories,
                LocalDateTime.now()
        );

        when(vehicleService.updateVehicle(eq(1L), any(VehicleDTO.class))).thenReturn(updatedVehicle);

        mockMvc.perform(put("/api/vehicles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVehicle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Zoe"))
                .andExpect(jsonPath("$.fuelType").value("Electric"))
                .andExpect(jsonPath("$.manufacturingYear").value(2024));

        verify(vehicleService, times(1)).updateVehicle(eq(1L), any(VehicleDTO.class));
    }

    @Test
    @DisplayName("PUT /api/vehicles/{id} - Update non-existent vehicle returns 404")
    void updateVehicle_NotFound_Returns404() throws Exception {
        when(vehicleService.updateVehicle(eq(999L), any(VehicleDTO.class)))
                .thenThrow(new ResourceNotFoundException("Vehicle not found with ID: 999"));

        mockMvc.perform(put("/api/vehicles/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testVehicleDTO)))
                .andExpect(status().isNotFound());

        verify(vehicleService, times(1)).updateVehicle(eq(999L), any(VehicleDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/vehicles/{id} - Delete vehicle successfully")
    void deleteVehicle_Success() throws Exception {
        doNothing().when(vehicleService).deleteVehicle(1L);

        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicle(1L);
    }

    @Test
    @DisplayName("DELETE /api/vehicles/{id} - Delete non-existent vehicle returns 404")
    void deleteVehicle_NotFound_Returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Vehicle not found with ID: 999"))
                .when(vehicleService).deleteVehicle(999L);

        mockMvc.perform(delete("/api/vehicles/999"))
                .andExpect(status().isNotFound());

        verify(vehicleService, times(1)).deleteVehicle(999L);
    }

    @Test
    @DisplayName("GET /api/vehicles/garage/{garageId} - Get vehicles by garage")
    void getVehiclesByGarage_Success() throws Exception {
        List<VehicleDTO> vehicles = List.of(testVehicleDTO);
        when(vehicleService.getVehiclesByGarage(1L)).thenReturn(vehicles);

        mockMvc.perform(get("/api/vehicles/garage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].brand").value("Renault"))
                .andExpect(jsonPath("$[0].model").value("Clio"))
                .andExpect(jsonPath("$[0].garageId").value(1));

        verify(vehicleService, times(1)).getVehiclesByGarage(1L);
    }

    @Test
    @DisplayName("GET /api/vehicles/garage/{garageId} - Get vehicles by garage returns empty list")
    void getVehiclesByGarage_EmptyList() throws Exception {
        when(vehicleService.getVehiclesByGarage(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/vehicles/garage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(vehicleService, times(1)).getVehiclesByGarage(1L);
    }

    @Test
    @DisplayName("GET /api/vehicles/model/{model} - Get vehicles by model")
    void getVehiclesByModel_Success() throws Exception {
        VehicleDTO vehicle2 = new VehicleDTO(
                2L,
                "Renault",
                2022,
                "Petrol",
                "Clio",
                2L,
                new ArrayList<>(),
                LocalDateTime.now()
        );

        List<VehicleDTO> vehicles = List.of(testVehicleDTO, vehicle2);
        when(vehicleService.getVehiclesByModel("Clio")).thenReturn(vehicles);

        mockMvc.perform(get("/api/vehicles/model/Clio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].model").value("Clio"))
                .andExpect(jsonPath("$[1].model").value("Clio"));

        verify(vehicleService, times(1)).getVehiclesByModel("Clio");
    }

    @Test
    @DisplayName("GET /api/vehicles/model/{model} - Get vehicles by model returns empty list")
    void getVehiclesByModel_EmptyList() throws Exception {
        when(vehicleService.getVehiclesByModel("NonExistentModel")).thenReturn(List.of());

        mockMvc.perform(get("/api/vehicles/model/NonExistentModel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(vehicleService, times(1)).getVehiclesByModel("NonExistentModel");
    }
}
