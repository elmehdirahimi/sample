package com.renault.renault.listener;

import com.renault.renault.event.VehicleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class VehicleCreatedEventListener {


    @EventListener
    public void onVehicleCreated(VehicleCreatedEvent event) {
        log.info("Vehicle created event received");
        log.info("Vehicle Details - Brand: {}, Model: {}, Fuel Type: {}, Manufacturing Year: {}, Garage ID: {}",
                event.getVehicleDTO().brand(),
                event.getVehicleDTO().model(),
                event.getVehicleDTO().fuelType(),
                event.getVehicleDTO().manufacturingYear(),
                event.getVehicleDTO().garageId());
        
        processVehicleCreationEvent(event);
    }


    private void processVehicleCreationEvent(VehicleCreatedEvent event) {
        try {
            log.debug("Processing vehicle creation event for vehicle with garage ID: {}",
                    event.getVehicleDTO().garageId());
            

        } catch (Exception e) {
            log.error("Error processing vehicle creation event", e);
        }
    }
}
