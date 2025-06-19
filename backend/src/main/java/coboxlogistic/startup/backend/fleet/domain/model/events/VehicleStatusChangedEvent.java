package coboxlogistic.startup.backend.fleet.domain.model.events;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleStatus;

import java.time.LocalDateTime;

/**
 * Domain Event fired when a vehicle's status changes in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class VehicleStatusChangedEvent {

    private final VehicleId vehicleId;
    private final VehicleStatus previousStatus;
    private final VehicleStatus newStatus;
    private final LocalDateTime occurredAt;

    public VehicleStatusChangedEvent(VehicleId vehicleId, VehicleStatus previousStatus, VehicleStatus newStatus) {
        this.vehicleId = vehicleId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.occurredAt = LocalDateTime.now();
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public VehicleStatus getPreviousStatus() {
        return previousStatus;
    }

    public VehicleStatus getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String toString() {
        return "VehicleStatusChangedEvent{" +
                "vehicleId=" + vehicleId +
                ", previousStatus=" + previousStatus +
                ", newStatus=" + newStatus +
                ", occurredAt=" + occurredAt +
                '}';
    }
} 