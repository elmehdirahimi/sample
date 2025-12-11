package com.renault.renault.service.impl;

import com.renault.renault.dto.vehicle.VehicleDTO;
import com.renault.renault.entity.Garage;
import com.renault.renault.entity.Vehicle;
import com.renault.renault.exception.BusinessConstraintViolationException;
import com.renault.renault.exception.ResourceNotFoundException;
import com.renault.renault.mapper.VehicleMapper;
import com.renault.renault.repository.GarageRepository;
import com.renault.renault.repository.VehicleRepository;
import com.renault.renault.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper vehicleMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public VehicleDTO addVehicle(Long garageId, VehicleDTO vehicleDTO) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with ID: " + garageId));
        if (!garage.canAddVehicle()) {
            throw new BusinessConstraintViolationException("Garage has reached the maximum limit of 50 vehicles");
        }
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle.setId(null);
        vehicle.setGarage(garage);
        Vehicle saved = vehicleRepository.save(vehicle);
        garage.addVehicle();
        garageRepository.save(garage);
        VehicleDTO createdDTO = vehicleMapper.toDto(saved);
        eventPublisher.publishEvent(new com.renault.renault.event.VehicleCreatedEvent(this, createdDTO));
        return createdDTO;
    }

    @Override
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        vehicle.setBrand(vehicleDTO.brand());
        vehicle.setManufacturingYear(vehicleDTO.manufacturingYear());
        vehicle.setFuelType(vehicleDTO.fuelType());
        vehicle.setModel(vehicleDTO.model());
        Vehicle saved = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(saved);
    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
        Garage garage = vehicle.getGarage();
        vehicleRepository.delete(vehicle);
        if (garage != null) {
            garage.removeVehicle();
            garageRepository.save(garage);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByGarage(Long garageId) {
        return vehicleRepository.findByGarage_Id(garageId)
                .stream().map(vehicleMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByModel(String model) {
        return vehicleRepository.findByModel(model)
                .stream().map(vehicleMapper::toDto).collect(Collectors.toList());
    }
}
