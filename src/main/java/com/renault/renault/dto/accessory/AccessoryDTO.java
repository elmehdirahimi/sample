package com.renault.renault.dto.accessory;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for Accessory entity (response).
 */
@Schema(description = "Accessory information response")
public record AccessoryDTO(
    @Schema(description = "Unique identifier of the accessory", example = "1")
    Long id,

    @Schema(description = "Accessory name", example = "GPS Navigation System")
    String name,

    @Schema(description = "Detailed description of the accessory", example = "Advanced GPS navigation with real-time traffic updates")
    String description,

    @Schema(description = "Price of the accessory in EUR", example = "299.99")
    Double price,

    @Schema(description = "Type of accessory", example = "Navigation")
    String type
) {}
