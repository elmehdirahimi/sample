package com.renault.renault.dto.vehicle;

import com.renault.renault.dto.accessory.AccessoryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Vehicle information response")
public record VehicleDTO(
    @Schema(description = "Unique identifier of the vehicle", example = "1")
    @NotNull(message = "Vehicle ID is required")
    @Positive(message = "Vehicle ID must be a positive number")
    Long id,

    @Schema(description = "Vehicle brand", example = "Renault")
    @NotBlank(message = "Brand is required and cannot be empty")
    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    String brand,

    @Schema(description = "Manufacturing year", example = "2023")
    @NotNull(message = "Manufacturing year is required")
    @Min(value = 1900, message = "Manufacturing year must be at least 1900")
    @Max(value = 2100, message = "Manufacturing year cannot exceed 2100")
    Integer manufacturingYear,

    @Schema(description = "Fuel type", example = "Diesel")
    @NotBlank(message = "Fuel type is required and cannot be empty")
    @Size(min = 3, max = 50, message = "Fuel type must be between 3 and 50 characters")
    String fuelType,

    @Schema(description = "Vehicle model name", example = "Clio")
    @NotBlank(message = "Model is required and cannot be empty")
    @Size(min = 2, max = 50, message = "Model must be between 2 and 50 characters")
    String model,

    @Schema(description = "ID of the garage where the vehicle is stored", example = "1")
    @NotNull(message = "Garage ID is required")
    @Positive(message = "Garage ID must be a positive number")
    Long garageId,

    @Schema(description = "List of accessories installed on the vehicle")
    @Valid
    List<AccessoryDTO> accessories,

    @Schema(description = "Timestamp when the vehicle was added to the system", example = "2023-12-11T10:30:00")
    @NotNull(message = "Creation timestamp is required")
    LocalDateTime createdAt
) {}
