package com.renault.renault.dto.common;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for OpeningTime entity.
 */
public record OpeningTimeDTO(
    Long id,
    LocalTime startTime,
    LocalTime endTime,
    DayOfWeek dayOfWeek
) {}
