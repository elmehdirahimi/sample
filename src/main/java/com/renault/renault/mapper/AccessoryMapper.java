package com.renault.renault.mapper;


import com.renault.renault.dto.accessory.AccessoryDTO;
import com.renault.renault.entity.Accessory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {
    AccessoryDTO toDto(Accessory accessory);
    Accessory toEntity(AccessoryDTO accessoryDTO);
}
