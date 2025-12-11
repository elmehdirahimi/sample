package com.renault.renault.listener;

import com.renault.renault.event.VehicleCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listener for VehicleCreatedEvent.
 * Handles business logic when a new vehicle is created.
 */
@Component
@Slf4j
public class VehicleCreatedEventListener {

    /**
     * Listens to VehicleCreatedEvent and processes the event.
     * Currently logs the vehicle creation with its details.
     * This can be extended to:
     * - Send notifications to administrators
     * - Update analytics
     * - Trigger inventory synchronization
     * - Send email confirmations
     *
     * @param event the vehicle created event containing vehicle details
     */
    @EventListener
    public void onVehicleCreated(VehicleCreatedEvent event) {
        log.info("Vehicle created event received");
        log.info("Vehicle Details - Brand: {}, Model: {}, Fuel Type: {}, Manufacturing Year: {}, Garage ID: {}",
                event.getVehicleDTO().brand(),
                event.getVehicleDTO().model(),
                event.getVehicleDTO().fuelType(),
                event.getVehicleDTO().manufacturingYear(),
                event.getVehicleDTO().garageId());
        
        // Additional business logic can be added here
        // Example: Send event to message queue, update cache, etc.
        processVehicleCreationEvent(event);
    }

    /**
     * Process the vehicle creation event with additional business logic.
     *
     * @param event the vehicle created event
     */
    private void processVehicleCreationEvent(VehicleCreatedEvent event) {
        try {
            // Add any additional processing here
            log.debug("Processing vehicle creation event for vehicle with garage ID: {}", 
                    event.getVehicleDTO().garageId());
            
            // Placeholder for future enhancements:
            // - Update search indices
            // - Trigger inventory updates
            // - Send notifications
            // - Log to audit trail
            
        } catch (Exception e) {
            log.error("Error processing vehicle creation event", e);
        }
    }
}
