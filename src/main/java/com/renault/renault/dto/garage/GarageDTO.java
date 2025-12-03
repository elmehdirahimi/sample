package com.renault.renault.dto.garage;

import com.renault.renault.dto.common.OpeningTimeDTO;

import java.util.Set;

/**
 * DTO for Garage entity (response).
 */
public record GarageDTO(
    Long id,
    String name,
    String address,
    String telephone,
    String email,
    Integer vehicleCount,
    int maxVehicles,
    Set<OpeningTimeDTO> openingTimes
) {}
