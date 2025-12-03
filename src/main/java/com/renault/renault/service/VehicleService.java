package com.renault.renault.service;

import com.renault.renault.dto.vehicle.VehicleDTO;

import java.util.List;

public interface VehicleService {
    VehicleDTO addVehicle(Long garageId, VehicleDTO vehicleDTO);
    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);
    void deleteVehicle(Long id);
    List<VehicleDTO> getVehiclesByGarage(Long garageId);
    List<VehicleDTO> getVehiclesByModel(String model);
}
