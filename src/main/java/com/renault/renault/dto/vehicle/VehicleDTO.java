package com.renault.renault.dto.vehicle;

import com.renault.renault.dto.accessory.AccessoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Vehicle entity (response).
 */
@Schema(description = "Vehicle information response")
public record VehicleDTO(
    @Schema(description = "Unique identifier of the vehicle", example = "1")
    Long id,

    @Schema(description = "Vehicle brand", example = "Renault")
    String brand,

    @Schema(description = "Manufacturing year", example = "2023")
    Integer manufacturingYear,

    @Schema(description = "Fuel type", example = "Diesel")
    String fuelType,

    @Schema(description = "Vehicle model name", example = "Clio")
    String model,

    @Schema(description = "ID of the garage where the vehicle is stored", example = "1")
    Long garageId,

    @Schema(description = "List of accessories installed on the vehicle")
    List<AccessoryDTO> accessories,

    @Schema(description = "Timestamp when the vehicle was added to the system", example = "2023-12-11T10:30:00")
    LocalDateTime createdAt
) {}
