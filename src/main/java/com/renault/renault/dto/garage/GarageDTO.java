package com.renault.renault.dto.garage;

import com.renault.renault.dto.common.OpeningTimeDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * DTO for Garage entity (response).
 */
@Schema(description = "Garage information response")
public record GarageDTO(
    @Schema(description = "Unique identifier of the garage", example = "1")
    Long id,

    @Schema(description = "Garage name", example = "Garage Paris Centre")
    String name,

    @Schema(description = "Garage address", example = "123 Avenue des Champs-Élysées, 75008 Paris")
    String address,

    @Schema(description = "Garage telephone number", example = "+33123456789")
    String telephone,

    @Schema(description = "Garage email address", example = "contact@garage-paris.com")
    String email,

    @Schema(description = "Current number of vehicles in the garage", example = "25")
    Integer vehicleCount,

    @Schema(description = "Maximum number of vehicles the garage can store", example = "50")
    int maxVehicles,

    @Schema(description = "Opening times for each day of the week")
    Set<OpeningTimeDTO> openingTimes
) {}
