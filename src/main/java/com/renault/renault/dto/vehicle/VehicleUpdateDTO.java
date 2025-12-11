package com.renault.renault.dto.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request DTO for updating an existing vehicle")
public record VehicleUpdateDTO(
    @Schema(description = "Vehicle brand", example = "Renault")
    @NotBlank(message = "Brand is required and cannot be empty")
    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    String brand,

    @Schema(description = "Manufacturing year", example = "2023")
    @NotNull(message = "Manufacturing year is required")
    @Min(value = 1900, message = "Manufacturing year must be at least 1900")
    @Max(value = 2100, message = "Manufacturing year cannot exceed 2100")
    Integer manufacturingYear,

    @Schema(description = "Fuel type (e.g., Essence, Diesel, Électrique, Hybride)", example = "Diesel")
    @NotBlank(message = "Fuel type is required and cannot be empty")
    @Size(min = 3, max = 50, message = "Fuel type must be between 3 and 50 characters")
    String fuelType,

    @Schema(description = "Vehicle model name", example = "Clio")
    @NotBlank(message = "Model is required and cannot be empty")
    @Size(min = 2, max = 50, message = "Model must be between 2 and 50 characters")
    String model
) {}
