package coboxlogistic.startup.backend.fleet.domain.exceptions;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

/**
 * Domain Exception thrown when a vehicle is not found in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class VehicleNotFoundException extends RuntimeException {

    private final VehicleId vehicleId;

    public VehicleNotFoundException(VehicleId vehicleId) {
        super("Vehicle not found with ID: " + vehicleId);
        this.vehicleId = vehicleId;
    }

    public VehicleNotFoundException(String message, VehicleId vehicleId) {
        super(message);
        this.vehicleId = vehicleId;
    }

    public VehicleNotFoundException(String message, VehicleId vehicleId, Throwable cause) {
        super(message, cause);
        this.vehicleId = vehicleId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }
} 