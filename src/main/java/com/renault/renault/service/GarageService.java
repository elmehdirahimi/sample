package com.renault.renault.service;

import com.renault.renault.dto.garage.GarageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GarageService {
    GarageDTO createGarage(GarageDTO garageDTO);
    GarageDTO updateGarage(Long id, GarageDTO garageDTO);
    void deleteGarage(Long id);
    GarageDTO getGarageById(Long id);
    Page<GarageDTO> getAllGarages(Pageable pageable, String sortBy);
    List<GarageDTO> searchGaragesByName(String name);
    List<GarageDTO> searchGaragesByVehicleModel(String model);
    List<GarageDTO> searchGaragesByFuelType(String fuelType);
    List<GarageDTO> searchGaragesByAccessory(String accessoryName);
}
