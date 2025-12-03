package com.renault.renault.dto.garage;

import com.renault.renault.dto.common.OpeningTimeDTO;

import java.util.Set;

/**
 * DTO for Garage update (request).
 */
public record GarageUpdateDTO(
    String name,
    String address,
    String telephone,
    String email,
    Set<OpeningTimeDTO> openingTimes
) {}
