package com.renault.renault.controller;

import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.service.AccessoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing accessories of vehicles.
 * Provides endpoints to add, update, delete, and list accessories for a vehicle.
 */
@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
@Tag(name = "Accessory Management", description = "APIs for managing vehicle accessories")
public class AccessoryController {
    private final AccessoryService accessoryService;

    /**
     * Add an accessory to a vehicle.
     */
    @PostMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Add an accessory to a vehicle",
            description = "Creates and associates a new accessory with the specified vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accessory added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccessoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AccessoryDTO> addAccessory(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable Long vehicleId,
            @Valid @RequestBody AccessoryDTO accessoryDTO) {
        return ResponseEntity.ok(accessoryService.addAccessory(vehicleId, accessoryDTO));
    }

    /**
     * Update an accessory by its ID.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an accessory",
            description = "Updates the details of an existing accessory (name, description, price, type)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accessory updated successfully"),
            @ApiResponse(responseCode = "404", description = "Accessory not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AccessoryDTO> updateAccessory(
            @Parameter(description = "Accessory ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AccessoryDTO accessoryDTO) {
        return ResponseEntity.ok(accessoryService.updateAccessory(id, accessoryDTO));
    }

    /**
     * Delete an accessory by its ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an accessory",
            description = "Removes an accessory from a vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Accessory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Accessory not found")
    })
    public ResponseEntity<Void> deleteAccessory(
            @Parameter(description = "Accessory ID", required = true) @PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all accessories for a given vehicle.
     */
    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get accessories by vehicle",
            description = "Retrieves all accessories associated with a specific vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accessories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<List<AccessoryDTO>> getAccessoriesByVehicle(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable Long vehicleId) {
        return ResponseEntity.ok(accessoryService.getAccessoriesByVehicle(vehicleId));
    }
}
