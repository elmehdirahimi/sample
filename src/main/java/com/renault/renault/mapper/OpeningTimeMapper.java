package com.renault.renault.mapper;


import com.renault.renault.dto.common.OpeningTimeDTO;
import com.renault.renault.entity.OpeningTime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {
    OpeningTimeDTO toDto(OpeningTime openingTime);
    OpeningTime toEntity(OpeningTimeDTO openingTimeDTO);
}
