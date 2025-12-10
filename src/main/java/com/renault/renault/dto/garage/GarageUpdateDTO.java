package com.renault.renault.dto.garage;

import com.renault.renault.dto.common.OpeningTimeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Set;

/**
 * DTO for Garage update (request).
 */
@Schema(description = "Request DTO for updating an existing garage")
public record GarageUpdateDTO(
    @Schema(description = "Garage name", example = "Garage Paris Centre")
    @NotBlank(message = "Garage name is required and cannot be empty")
    @Size(min = 3, max = 100, message = "Garage name must be between 3 and 100 characters")
    String name,

    @Schema(description = "Garage address", example = "123 Avenue des Champs-Élysées, 75008 Paris")
    @NotBlank(message = "Address is required and cannot be empty")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    String address,

    @Schema(description = "Garage telephone number", example = "+33123456789")
    @NotBlank(message = "Telephone is required and cannot be empty")
    @Pattern(regexp = "^\\+?[0-9\\s\\-()]+$", message = "Telephone must be a valid phone number format")
    String telephone,

    @Schema(description = "Garage email address", example = "contact@garage-paris.com")
    @NotBlank(message = "Email is required and cannot be empty")
    @Email(message = "Email must be a valid email address")
    String email,

    @Schema(description = "Opening times for each day of the week")
    @NotEmpty(message = "At least one opening time entry is required")
    @Valid
    Set<OpeningTimeDTO> openingTimes
) {}
