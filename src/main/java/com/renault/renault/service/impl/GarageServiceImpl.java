package com.renault.renault.service.impl;

import com.renault.renault.dto.garage.GarageDTO;
import com.renault.renault.entity.Garage;
import com.renault.renault.mapper.GarageMapper;
import com.renault.renault.repository.GarageRepository;
import com.renault.renault.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GarageServiceImpl implements GarageService {
    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    @Override
    public GarageDTO createGarage(GarageDTO garageDTO) {
        Garage garage = garageMapper.toEntity(garageDTO);
        garage.setId(null);
        garage.setVehicleCount(0);
        Garage saved = garageRepository.save(garage);
        return garageMapper.toDto(saved);
    }

    @Override
    public GarageDTO updateGarage(Long id, GarageDTO garageDTO) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Garage not found"));
        garage.setName(garageDTO.name());
        garage.setAddress(garageDTO.address());
        garage.setTelephone(garageDTO.telephone());
        garage.setEmail(garageDTO.email());
        Garage saved = garageRepository.save(garage);
        return garageMapper.toDto(saved);
    }

    @Override
    public void deleteGarage(Long id) {
        garageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public GarageDTO getGarageById(Long id) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Garage not found"));
        return garageMapper.toDto(garage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageDTO> getAllGarages(Pageable pageable, String sortBy) {
        Page<Garage> garages = garageRepository.findAll(pageable);
        List<GarageDTO> dtos = garages.stream()
                .map(garageMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, garages.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDTO> searchGaragesByName(String name) {
        return garageRepository.findByNameContainingIgnoreCase(name)
                .stream().map(garageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDTO> searchGaragesByVehicleModel(String model) {
        return garageRepository.findByVehicles_Model(model)
                .stream().map(garageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDTO> searchGaragesByFuelType(String fuelType) {
        return garageRepository.findByVehicles_FuelType(fuelType)
                .stream().map(garageMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDTO> searchGaragesByAccessory(String accessoryName) {
        return garageRepository.findByVehicles_Accessories_Name(accessoryName)
                .stream().map(garageMapper::toDto).collect(Collectors.toList());
    }
}
