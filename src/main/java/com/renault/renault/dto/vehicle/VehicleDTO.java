package com.renault.renault.dto.vehicle;

import com.renault.renault.dto.accessory.AccessoryDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Vehicle entity (response).
 */
public record VehicleDTO(
    Long id,
    String brand,
    Integer manufacturingYear,
    String fuelType,
    String model,
    Long garageId,
    List<AccessoryDTO> accessories,
    LocalDateTime createdAt
) {}
