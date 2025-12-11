package com.renault.renault.controller;

import com.renault.renault.dto.garage.GarageDTO;
import com.renault.renault.service.GarageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
@Tag(name = "Garage Management", description = "APIs for managing Renault affiliated garages")
public class GarageController {
    private final GarageService garageService;

    @PostMapping
    @Operation(summary = "Create a new garage",
            description = "Creates a new garage with the provided details including opening times")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Garage created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GarageDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GarageDTO> createGarage(@Valid @RequestBody GarageDTO garageDTO) {
        return ResponseEntity.ok(garageService.createGarage(garageDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing garage",
            description = "Updates garage details including name, address, contact information and opening times")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Garage updated successfully"),
            @ApiResponse(responseCode = "404", description = "Garage not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<GarageDTO> updateGarage(
            @Parameter(description = "Garage ID", required = true) @PathVariable Long id,
            @Valid @RequestBody GarageDTO garageDTO) {
        return ResponseEntity.ok(garageService.updateGarage(id, garageDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a garage",
            description = "Deletes a garage and all associated vehicles and accessories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Garage deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Garage not found")
    })
    public ResponseEntity<Void> deleteGarage(
            @Parameter(description = "Garage ID", required = true) @PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get garage by ID",
            description = "Retrieves detailed information about a specific garage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Garage found"),
            @ApiResponse(responseCode = "404", description = "Garage not found")
    })
    public ResponseEntity<GarageDTO> getGarageById(
            @Parameter(description = "Garage ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(garageService.getGarageById(id));
    }

    @GetMapping
    @Operation(summary = "Get all garages with pagination",
            description = "Retrieves a paginated list of all garages with optional sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sorting parameters")
    })
    public ResponseEntity<Page<GarageDTO>> getAllGarages(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (name, address, email, etc.)") @RequestParam(defaultValue = "name") String sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(garageService.getAllGarages(pageable, sortBy));
    }

    //TODO: fix search
    @GetMapping("/search")
    @Operation(summary = "Search garages by criteria",
            description = "Search for garages by name, vehicle model, fuel type, or accessory availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid search parameters")
    })
    public ResponseEntity<List<GarageDTO>> searchGarages(
            @Parameter(description = "Garage name (contains search)") @RequestParam(required = false) String name,
            @Parameter(description = "Vehicle model") @RequestParam(required = false) String model,
            @Parameter(description = "Fuel type") @RequestParam(required = false) String fuelType,
            @Parameter(description = "Accessory name") @RequestParam(required = false) String accessory) {
        if (name != null) {
            return ResponseEntity.ok(garageService.searchGaragesByName(name));
        } else if (model != null) {
            return ResponseEntity.ok(garageService.searchGaragesByVehicleModel(model));
        } else if (fuelType != null) {
            return ResponseEntity.ok(garageService.searchGaragesByFuelType(fuelType));
        } else if (accessory != null) {
            return ResponseEntity.ok(garageService.searchGaragesByAccessory(accessory));
        }
        return ResponseEntity.badRequest().build();
    }
}
