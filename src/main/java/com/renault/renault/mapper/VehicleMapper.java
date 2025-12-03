package com.renault.renault.mapper;

import com.renault.renault.dto.vehicle.VehicleDTO;
import com.renault.renault.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleDTO toDto(Vehicle vehicle);
    Vehicle toEntity(VehicleDTO vehicleDTO);
}
