package coboxlogistic.startup.backend.fleet.domain.model.events;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelEfficiency;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.time.LocalDateTime;

/**
 * Domain Event fired when a new fuel record is created in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class FuelRecordCreatedEvent {

    private final String fuelRecordId;
    private final VehicleId vehicleId;
    private final FuelEfficiency efficiency;
    private final LocalDateTime occurredAt;

    public FuelRecordCreatedEvent(String fuelRecordId, VehicleId vehicleId, FuelEfficiency efficiency) {
        this.fuelRecordId = fuelRecordId;
        this.vehicleId = vehicleId;
        this.efficiency = efficiency;
        this.occurredAt = LocalDateTime.now();
    }

    public String getFuelRecordId() {
        return fuelRecordId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public FuelEfficiency getEfficiency() {
        return efficiency;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String toString() {
        return "FuelRecordCreatedEvent{" +
                "fuelRecordId='" + fuelRecordId + '\'' +
                ", vehicleId=" + vehicleId +
                ", efficiency=" + efficiency +
                ", occurredAt=" + occurredAt +
                '}';
    }
} 