package com.renault.renault.dto.vehicle;

/**
 * DTO for Vehicle creation (request).
 */
public record VehicleCreateDTO(
    String brand,
    Integer manufacturingYear,
    String fuelType,
    String model
) {}
