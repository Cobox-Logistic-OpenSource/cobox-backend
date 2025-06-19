package coboxlogistic.startup.backend.fleet.domain.model.aggregates;

import coboxlogistic.startup.backend.fleet.domain.model.events.MileageRecordCreatedEvent;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.MileagePurpose;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import coboxlogistic.startup.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * MileageRecord Aggregate Root in the Fleet bounded context.
 * Manages mileage tracking and distance calculations.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Entity
@Table(name = "mileage_records")
@Getter
@NoArgsConstructor
public class MileageRecord extends AuditableAbstractAggregateRoot<String> {

    @Id
    private String id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "vehicle_id", nullable = false))
    })
    private VehicleId vehicleId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "start_odometer", nullable = false)
    private BigDecimal startOdometer;

    @Column(name = "end_odometer", nullable = false)
    private BigDecimal endOdometer;

    @Column(name = "distance", nullable = false)
    private BigDecimal distance;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "purpose", nullable = false))
    })
    private MileagePurpose purpose;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "route")
    private String route;

    @Column(name = "notes", length = 1000)
    private String notes;

    /**
     * Private constructor for creating a new mileage record
     */
    private MileageRecord(String id, VehicleId vehicleId, LocalDateTime date, BigDecimal startOdometer,
                         BigDecimal endOdometer, MileagePurpose purpose, String driverId, String route, String notes) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.date = date;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.distance = endOdometer.subtract(startOdometer);
        this.purpose = purpose;
        this.driverId = driverId;
        this.route = route;
        this.notes = notes;
    }

    /**
     * Factory method to create a new mileage record
     */
    public static MileageRecord create(VehicleId vehicleId, LocalDateTime date, BigDecimal startOdometer,
                                     BigDecimal endOdometer, MileagePurpose purpose, String driverId, 
                                     String route, String notes) {
        
        validateMileageRecordData(vehicleId, date, startOdometer, endOdometer, purpose);
        
        String id = UUID.randomUUID().toString();
        MileageRecord mileageRecord = new MileageRecord(id, vehicleId, date, startOdometer,
                                                      endOdometer, purpose, driverId, route, notes);
        
        // Add domain event
        mileageRecord.addDomainEvent(new MileageRecordCreatedEvent(mileageRecord.getId(), 
                                                                  mileageRecord.getVehicleId(), 
                                                                  mileageRecord.getDistance()));
        
        return mileageRecord;
    }

    /**
     * Update mileage record
     */
    public void update(LocalDateTime date, BigDecimal startOdometer, BigDecimal endOdometer,
                      MileagePurpose purpose, String driverId, String route, String notes) {
        
        validateMileageRecordData(this.vehicleId, date, startOdometer, endOdometer, purpose);
        
        this.date = date;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.distance = endOdometer.subtract(startOdometer);
        this.purpose = purpose;
        this.driverId = driverId;
        this.route = route;
        this.notes = notes;
    }

    /**
     * Check if this is a business-related trip
     */
    public boolean isBusinessRelated() {
        return purpose != null && purpose.isBusinessRelated();
    }

    /**
     * Check if this is an operational trip
     */
    public boolean isOperational() {
        return purpose != null && purpose.isOperational();
    }

    /**
     * Check if this is a maintenance-related trip
     */
    public boolean isMaintenanceRelated() {
        return purpose != null && purpose.isMaintenanceRelated();
    }

    /**
     * Get average speed (if time information is available)
     */
    public BigDecimal getAverageSpeed(BigDecimal hoursTraveled) {
        if (hoursTraveled == null || hoursTraveled.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return distance.divide(hoursTraveled, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Validate mileage record data
     */
    private static void validateMileageRecordData(VehicleId vehicleId, LocalDateTime date, 
                                                BigDecimal startOdometer, BigDecimal endOdometer, 
                                                MileagePurpose purpose) {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        if (startOdometer == null || startOdometer.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Start odometer must be positive");
        }
        if (endOdometer == null || endOdometer.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("End odometer must be positive");
        }
        if (endOdometer.compareTo(startOdometer) <= 0) {
            throw new IllegalArgumentException("End odometer must be greater than start odometer");
        }
        if (purpose == null) {
            throw new IllegalArgumentException("Purpose cannot be null");
        }
    }

    @Override
    public String getId() {
        return this.id;
    }
} 