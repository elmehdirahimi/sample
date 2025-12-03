package com.renault.renault.controller;

import com.renault.renault.dto.vehicle.VehicleDTO;
import com.renault.renault.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/garage/{garageId}")
    public ResponseEntity<VehicleDTO> addVehicle(@PathVariable Long garageId, @Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.addVehicle(garageId, vehicleDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByGarage(@PathVariable Long garageId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByGarage(garageId));
    }

    @GetMapping("/model/{model}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByModel(@PathVariable String model) {
        return ResponseEntity.ok(vehicleService.getVehiclesByModel(model));
    }
}
