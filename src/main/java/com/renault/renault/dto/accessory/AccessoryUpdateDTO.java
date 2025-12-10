package com.renault.renault.dto.accessory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * DTO for Accessory update (request).
 */
@Schema(description = "Request DTO for updating an existing accessory")
public record AccessoryUpdateDTO(
    @Schema(description = "Accessory name", example = "GPS Navigation System")
    @NotBlank(message = "Name is required and cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @Schema(description = "Detailed description of the accessory", example = "Advanced GPS navigation with real-time traffic updates")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description,

    @Schema(description = "Price of the accessory in EUR", example = "299.99")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price cannot exceed 999999.99")
    Double price,

    @Schema(description = "Type of accessory (e.g., Navigation, Audio, Comfort, Protection)", example = "Navigation")
    @NotBlank(message = "Type is required and cannot be empty")
    @Size(min = 2, max = 50, message = "Type must be between 2 and 50 characters")
    String type
) {}
