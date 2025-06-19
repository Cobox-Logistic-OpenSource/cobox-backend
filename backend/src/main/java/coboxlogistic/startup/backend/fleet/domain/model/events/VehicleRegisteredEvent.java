package coboxlogistic.startup.backend.fleet.domain.model.events;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.LicensePlate;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.time.LocalDateTime;

/**
 * Domain Event fired when a new vehicle is registered in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class VehicleRegisteredEvent {

    private final VehicleId vehicleId;
    private final LicensePlate licensePlate;
    private final LocalDateTime occurredAt;

    public VehicleRegisteredEvent(VehicleId vehicleId, LicensePlate licensePlate) {
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.occurredAt = LocalDateTime.now();
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public LicensePlate getLicensePlate() {
        return licensePlate;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String toString() {
        return "VehicleRegisteredEvent{" +
                "vehicleId=" + vehicleId +
                ", licensePlate=" + licensePlate +
                ", occurredAt=" + occurredAt +
                '}';
    }
} 