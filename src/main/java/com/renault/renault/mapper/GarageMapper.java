package com.renault.renault.mapper;


import com.renault.renault.dto.garage.GarageDTO;
import com.renault.renault.entity.Garage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GarageMapper {
    GarageDTO toDto(Garage garage);
    Garage toEntity(GarageDTO garageDTO);
}
