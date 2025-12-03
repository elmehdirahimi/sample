package com.renault.renault.dto.accessory;

/**
 * DTO for Accessory entity (response).
 */
public record AccessoryDTO(
    Long id,
    String name,
    String description,
    Double price,
    String type
) {}
