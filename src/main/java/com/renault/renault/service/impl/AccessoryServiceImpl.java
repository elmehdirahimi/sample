package com.renault.renault.service.impl;

import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.entity.Accessory;
import com.renault.renault.entity.Vehicle;
import com.renault.renault.exception.ResourceNotFoundException;
import com.renault.renault.mapper.AccessoryMapper;
import com.renault.renault.repository.AccessoryRepository;
import com.renault.renault.repository.VehicleRepository;
import com.renault.renault.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryServiceImpl implements AccessoryService {
    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryDTO addAccessory(Long vehicleId, AccessoryDTO accessoryDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
        Accessory accessory = accessoryMapper.toEntity(accessoryDTO);
        accessory.setId(null);
        accessory.setVehicle(vehicle);
        Accessory saved = accessoryRepository.save(accessory);
        return accessoryMapper.toDto(saved);
    }

    @Override
    public AccessoryDTO updateAccessory(Long id, AccessoryDTO accessoryDTO) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with ID: " + id));
        accessory.setName(accessoryDTO.name());
        accessory.setDescription(accessoryDTO.description());
        accessory.setPrice(accessoryDTO.price());
        accessory.setType(accessoryDTO.type());
        Accessory saved = accessoryRepository.save(accessory);
        return accessoryMapper.toDto(saved);
    }

    @Override
    public void deleteAccessory(Long id) {
        accessoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoryDTO> getAccessoriesByVehicle(Long vehicleId) {
        return accessoryRepository.findByVehicle_Id(vehicleId)
                .stream().map(accessoryMapper::toDto).collect(Collectors.toList());
    }
}
