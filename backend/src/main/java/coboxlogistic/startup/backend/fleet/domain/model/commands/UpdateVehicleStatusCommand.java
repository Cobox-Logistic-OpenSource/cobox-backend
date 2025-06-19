package coboxlogistic.startup.backend.fleet.domain.model.commands;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleStatus;

/**
 * Command to update a vehicle's status in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record UpdateVehicleStatusCommand(
    VehicleId vehicleId,
    VehicleStatus newStatus
) {
    
    public UpdateVehicleStatusCommand {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }
    }
} 