package coboxlogistic.startup.backend.fleet.domain.model.events;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain Event fired when a new mileage record is created in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class MileageRecordCreatedEvent {

    private final String mileageRecordId;
    private final VehicleId vehicleId;
    private final BigDecimal distance;
    private final LocalDateTime occurredAt;

    public MileageRecordCreatedEvent(String mileageRecordId, VehicleId vehicleId, BigDecimal distance) {
        this.mileageRecordId = mileageRecordId;
        this.vehicleId = vehicleId;
        this.distance = distance;
        this.occurredAt = LocalDateTime.now();
    }

    public String getMileageRecordId() {
        return mileageRecordId;
    }

    public VehicleId getVehicleId() {
        return vehicleId;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String toString() {
        return "MileageRecordCreatedEvent{" +
                "mileageRecordId='" + mileageRecordId + '\'' +
                ", vehicleId=" + vehicleId +
                ", distance=" + distance +
                ", occurredAt=" + occurredAt +
                '}';
    }
} 