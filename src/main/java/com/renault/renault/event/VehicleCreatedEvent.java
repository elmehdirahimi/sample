package com.renault.renault.event;

import com.renault.renault.dto.vehicle.VehicleDTO;
import org.springframework.context.ApplicationEvent;

public class VehicleCreatedEvent extends ApplicationEvent {
    private final VehicleDTO vehicleDTO;

    public VehicleCreatedEvent(Object source, VehicleDTO vehicleDTO) {
        super(source);
        this.vehicleDTO = vehicleDTO;
    }

    public VehicleDTO getVehicleDTO() {
        return vehicleDTO;
    }
}
