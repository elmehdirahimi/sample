package com.renault.renault.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for OpeningTime entity.
 */
@Schema(description = "Opening time information for a specific day")
public record OpeningTimeDTO(
    @Schema(description = "Opening time ID (null for new entries)")
    Long id,

    @Schema(description = "Start time of the opening period", example = "09:00")
    @NotNull(message = "Start time is required")
    LocalTime startTime,

    @Schema(description = "End time of the opening period", example = "18:00")
    @NotNull(message = "End time is required")
    LocalTime endTime,

    @Schema(description = "Day of the week", example = "MONDAY")
    @NotNull(message = "Day of week is required")
    DayOfWeek dayOfWeek
) {}
