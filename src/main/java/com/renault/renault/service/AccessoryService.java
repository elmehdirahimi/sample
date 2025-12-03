package com.renault.renault.service;

import com.renault.renault.dto.accessory.AccessoryDTO;

import java.util.List;

public interface AccessoryService {
    AccessoryDTO addAccessory(Long vehicleId, AccessoryDTO accessoryDTO);
    AccessoryDTO updateAccessory(Long id, AccessoryDTO accessoryDTO);
    void deleteAccessory(Long id);
    List<AccessoryDTO> getAccessoriesByVehicle(Long vehicleId);
}
