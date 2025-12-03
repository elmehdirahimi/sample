package com.renault.renault.dto.accessory;

/**
 * DTO for Accessory update (request).
 */
public record AccessoryUpdateDTO(
    String name,
    String description,
    Double price,
    String type
) {}
