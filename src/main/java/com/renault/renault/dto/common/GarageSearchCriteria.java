package com.renault.renault.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Search criteria for garage queries")
public record GarageSearchCriteria(
        @Schema(description = "Search by garage name (partial match, case-insensitive)", example = "Paris")
        @Size(min = 1, max = 100, message = "Search name must be between 1 and 100 characters")
        String name,

        @Schema(description = "Search by vehicle model stored in the garage", example = "Clio")
        @Size(min = 1, max = 50, message = "Search model must be between 1 and 50 characters")
        String model,

        @Schema(description = "Search by vehicle fuel type (e.g., DIESEL, PETROL, ELECTRIC)", example = "DIESEL")
        @Size(min = 1, max = 50, message = "Search fuel type must be between 1 and 50 characters")
        String fuelType,

        @Schema(description = "Search by accessory name available in the garage", example = "Spare Tire")
        @Size(min = 1, max = 100, message = "Search accessory must be between 1 and 100 characters")
        String accessory
) {
    /**
     * Checks if at least one search criterion is provided.
     * @return true if at least one field is not null, false otherwise
     */
    public boolean hasSearchCriteria() {
        return name != null || model != null || fuelType != null || accessory != null;
    }
}
