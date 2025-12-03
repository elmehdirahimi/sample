package com.renault.renault.controller;

import com.renault.renault.dto.garage.GarageDTO;
import com.renault.renault.service.GarageService;
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
public class GarageController {
    private final GarageService garageService;

    @PostMapping
    public ResponseEntity<GarageDTO> createGarage(@Valid @RequestBody GarageDTO garageDTO) {
        return ResponseEntity.ok(garageService.createGarage(garageDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageDTO> updateGarage(@PathVariable Long id, @Valid @RequestBody GarageDTO garageDTO) {
        return ResponseEntity.ok(garageService.updateGarage(id, garageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> getGarageById(@PathVariable Long id) {
        return ResponseEntity.ok(garageService.getGarageById(id));
    }

    @GetMapping
    public ResponseEntity<Page<GarageDTO>> getAllGarages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(garageService.getAllGarages(pageable, sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GarageDTO>> searchGarages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String accessory) {
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
