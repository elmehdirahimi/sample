package com.renault.renault.controller;

import com.renault.renault.dto.vehicle.VehicleDTO;
import com.renault.renault.service.VehicleService;
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

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles in garages")
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/garage/{garageId}")
    @Operation(summary = "Add a vehicle to a garage",
            description = "Adds a new vehicle to the specified garage (max 50 vehicles per garage)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "404", description = "Garage not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input or garage is at capacity")
    })
    public ResponseEntity<VehicleDTO> addVehicle(
            @Parameter(description = "Garage ID", required = true) @PathVariable Long garageId,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.addVehicle(garageId, vehicleDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vehicle",
            description = "Updates vehicle details such as brand, model, manufacturing year and fuel type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<VehicleDTO> updateVehicle(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable Long id,
            @Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle",
            description = "Deletes a vehicle and all associated accessories from the garage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vehicle deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<Void> deleteVehicle(
            @Parameter(description = "Vehicle ID", required = true) @PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/garage/{garageId}")
    @Operation(summary = "Get vehicles by garage",
            description = "Retrieves all vehicles stored in a specific garage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Garage not found")
    })
    public ResponseEntity<List<VehicleDTO>> getVehiclesByGarage(
            @Parameter(description = "Garage ID", required = true) @PathVariable Long garageId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByGarage(garageId));
    }

    @GetMapping("/model/{model}")
    @Operation(summary = "Get vehicles by model",
            description = "Retrieves all vehicles of a specific model across all garages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid model parameter")
    })
    public ResponseEntity<List<VehicleDTO>> getVehiclesByModel(
            @Parameter(description = "Vehicle model name", required = true) @PathVariable String model) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModel(model));
    }
}
