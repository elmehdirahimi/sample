package com.renault.renault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.exception.ResourceNotFoundException;
import com.renault.renault.service.AccessoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessoryController.class)
@DisplayName("AccessoryController Integration Tests")
class AccessoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccessoryService accessoryService;

    private AccessoryDTO testAccessoryDTO;

    @BeforeEach
    void setUp() {
        testAccessoryDTO = new AccessoryDTO(
                1L,
                "GPS Navigation System",
                "Advanced GPS navigation with real-time traffic updates",
                299.99,
                "Navigation"
        );
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory successfully")
    void addAccessory_Success() throws Exception {
        when(accessoryService.addAccessory(eq(1L), any(AccessoryDTO.class))).thenReturn(testAccessoryDTO);

        mockMvc.perform(post("/api/accessories/vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccessoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("GPS Navigation System"))
                .andExpect(jsonPath("$.description").value("Advanced GPS navigation with real-time traffic updates"))
                .andExpect(jsonPath("$.price").value(299.99))
                .andExpect(jsonPath("$.type").value("Navigation"));

        verify(accessoryService, times(1)).addAccessory(eq(1L), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory with invalid data returns 400")
    void addAccessory_InvalidData_Returns400() throws Exception {
        AccessoryDTO invalidAccessory = new AccessoryDTO(
                null,
                "",
                "Description",
                -50.0,
                "Navigation"
        );

        mockMvc.perform(post("/api/accessories/vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccessory)))
                .andExpect(status().isBadRequest());

        verify(accessoryService, never()).addAccessory(anyLong(), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory to non-existent vehicle returns 404")
    void addAccessory_VehicleNotFound_Returns404() throws Exception {
        when(accessoryService.addAccessory(eq(999L), any(AccessoryDTO.class)))
                .thenThrow(new ResourceNotFoundException("Vehicle not found with ID: 999"));

        mockMvc.perform(post("/api/accessories/vehicle/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccessoryDTO)))
                .andExpect(status().isNotFound());

        verify(accessoryService, times(1)).addAccessory(eq(999L), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("PUT /api/accessories/{id} - Update accessory successfully")
    void updateAccessory_Success() throws Exception {
        AccessoryDTO updatedAccessory = new AccessoryDTO(
                1L,
                "Updated GPS Navigation",
                "Updated description with new features",
                349.99,
                "Navigation"
        );

        when(accessoryService.updateAccessory(eq(1L), any(AccessoryDTO.class))).thenReturn(updatedAccessory);

        mockMvc.perform(put("/api/accessories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAccessory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated GPS Navigation"))
                .andExpect(jsonPath("$.description").value("Updated description with new features"))
                .andExpect(jsonPath("$.price").value(349.99));

        verify(accessoryService, times(1)).updateAccessory(eq(1L), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("PUT /api/accessories/{id} - Update non-existent accessory returns 404")
    void updateAccessory_NotFound_Returns404() throws Exception {
        when(accessoryService.updateAccessory(eq(999L), any(AccessoryDTO.class)))
                .thenThrow(new ResourceNotFoundException("Accessory not found with ID: 999"));

        mockMvc.perform(put("/api/accessories/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccessoryDTO)))
                .andExpect(status().isNotFound());

        verify(accessoryService, times(1)).updateAccessory(eq(999L), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("PUT /api/accessories/{id} - Update accessory with invalid data returns 400")
    void updateAccessory_InvalidData_Returns400() throws Exception {
        AccessoryDTO invalidAccessory = new AccessoryDTO(
                1L,
                "A",
                "Description",
                0.0,
                "Navigation"
        );

        mockMvc.perform(put("/api/accessories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAccessory)))
                .andExpect(status().isBadRequest());

        verify(accessoryService, never()).updateAccessory(anyLong(), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/accessories/{id} - Delete accessory successfully")
    void deleteAccessory_Success() throws Exception {
        doNothing().when(accessoryService).deleteAccessory(1L);

        mockMvc.perform(delete("/api/accessories/1"))
                .andExpect(status().isNoContent());

        verify(accessoryService, times(1)).deleteAccessory(1L);
    }

    @Test
    @DisplayName("GET /api/accessories/vehicle/{vehicleId} - Get accessories by vehicle")
    void getAccessoriesByVehicle_Success() throws Exception {
        AccessoryDTO accessory2 = new AccessoryDTO(
                2L,
                "Roof Rack",
                "Heavy-duty roof rack for cargo",
                199.99,
                "Cargo"
        );

        List<AccessoryDTO> accessories = List.of(testAccessoryDTO, accessory2);
        when(accessoryService.getAccessoriesByVehicle(1L)).thenReturn(accessories);

        mockMvc.perform(get("/api/accessories/vehicle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("GPS Navigation System"))
                .andExpect(jsonPath("$[0].type").value("Navigation"))
                .andExpect(jsonPath("$[1].name").value("Roof Rack"))
                .andExpect(jsonPath("$[1].type").value("Cargo"));

        verify(accessoryService, times(1)).getAccessoriesByVehicle(1L);
    }

    @Test
    @DisplayName("GET /api/accessories/vehicle/{vehicleId} - Get accessories by vehicle returns empty list")
    void getAccessoriesByVehicle_EmptyList() throws Exception {
        when(accessoryService.getAccessoriesByVehicle(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/accessories/vehicle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(accessoryService, times(1)).getAccessoriesByVehicle(1L);
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory with max price")
    void addAccessory_WithMaxPrice_Success() throws Exception {
        AccessoryDTO expensiveAccessory = new AccessoryDTO(
                1L,
                "Premium Sound System",
                "High-end audio system",
                999999.99,
                "Audio"
        );

        when(accessoryService.addAccessory(eq(1L), any(AccessoryDTO.class))).thenReturn(expensiveAccessory);

        mockMvc.perform(post("/api/accessories/vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensiveAccessory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(999999.99));

        verify(accessoryService, times(1)).addAccessory(eq(1L), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory with price exceeding max returns 400")
    void addAccessory_PriceExceedsMax_Returns400() throws Exception {
        AccessoryDTO overPricedAccessory = new AccessoryDTO(
                null,
                "Overpriced Item",
                "Too expensive",
                1000000.0,
                "Luxury"
        );

        mockMvc.perform(post("/api/accessories/vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(overPricedAccessory)))
                .andExpect(status().isBadRequest());

        verify(accessoryService, never()).addAccessory(anyLong(), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory with long description")
    void addAccessory_WithLongDescription_Success() throws Exception {
        String longDescription = "A".repeat(500); // Max length
        AccessoryDTO accessoryWithLongDesc = new AccessoryDTO(
                1L,
                "Accessory",
                longDescription,
                99.99,
                "Type"
        );

        when(accessoryService.addAccessory(eq(1L), any(AccessoryDTO.class))).thenReturn(accessoryWithLongDesc);

        mockMvc.perform(post("/api/accessories/vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessoryWithLongDesc)))
                .andExpect(status().isOk());

        verify(accessoryService, times(1)).addAccessory(eq(1L), any(AccessoryDTO.class));
    }

    @Test
    @DisplayName("POST /api/accessories/vehicle/{vehicleId} - Add accessory with description too long returns 400")
    void addAccessory_DescriptionTooLong_Returns400() throws Exception {
        String tooLongDescription = "A".repeat(501); // Exceeds max length
        AccessoryDTO accessoryWithTooLongDesc = new AccessoryDTO(
                null,
                "Accessory",
                tooLongDescription,
                99.99,
                "Type"
        );

        mockMvc.perform(post("/api/accessories/vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accessoryWithTooLongDesc)))
                .andExpect(status().isBadRequest());

        verify(accessoryService, never()).addAccessory(anyLong(), any(AccessoryDTO.class));
    }
}
