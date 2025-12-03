package com.renault.renault.dto.accessory;

/**
 * DTO for Accessory creation (request).
 */
public record AccessoryCreateDTO(
    String name,
    String description,
    Double price,
    String type
) {}
