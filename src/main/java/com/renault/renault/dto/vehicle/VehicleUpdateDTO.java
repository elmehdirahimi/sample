package com.renault.renault.dto.vehicle;

/**
 * DTO for Vehicle update (request).
 */
public record VehicleUpdateDTO(
    String brand,
    Integer manufacturingYear,
    String fuelType,
    String model
) {}
