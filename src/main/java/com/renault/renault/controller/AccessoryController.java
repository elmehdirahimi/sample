package com.renault.renault.controller;

import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.service.AccessoryService;
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
public class AccessoryController {
    private final AccessoryService accessoryService;

    /**
     * Add an accessory to a vehicle.
     */
    @PostMapping("/vehicle/{vehicleId}")
    public ResponseEntity<AccessoryDTO> addAccessory(@PathVariable Long vehicleId, @Valid @RequestBody AccessoryDTO accessoryDTO) {
        return ResponseEntity.ok(accessoryService.addAccessory(vehicleId, accessoryDTO));
    }

    /**
     * Update an accessory by its ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccessoryDTO> updateAccessory(@PathVariable Long id, @Valid @RequestBody AccessoryDTO accessoryDTO) {
        return ResponseEntity.ok(accessoryService.updateAccessory(id, accessoryDTO));
    }

    /**
     * Delete an accessory by its ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * List all accessories for a given vehicle.
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<AccessoryDTO>> getAccessoriesByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(accessoryService.getAccessoriesByVehicle(vehicleId));
    }
}
