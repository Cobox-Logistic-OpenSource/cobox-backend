package coboxlogistic.startup.backend.fleet.domain.model.aggregates;

import coboxlogistic.startup.backend.fleet.domain.model.events.FuelRecordCreatedEvent;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelEfficiency;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelType;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import coboxlogistic.startup.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * FuelRecord Aggregate Root in the Fleet bounded context.
 * Manages fuel consumption records and efficiency calculations.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Entity
@Table(name = "fuel_records")
@Getter
@NoArgsConstructor
public class FuelRecord extends AuditableAbstractAggregateRoot<String> {

    @Id
    private String id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "vehicle_id", nullable = false))
    })
    private VehicleId vehicleId;

    @Column(name = "vehicle_plate", nullable = false)
    private String vehiclePlate;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "fuel_type", nullable = false))
    })
    private FuelType fuelType;

    @Column(name = "quantity", nullable = false, precision = 8, scale = 2)
    private BigDecimal quantity;

    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "current_mileage", nullable = false)
    private BigDecimal currentMileage;

    @Column(name = "station")
    private String station;

    @Column(name = "location")
    private String location;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "efficiency_value", precision = 5, scale = 2)),
        @AttributeOverride(name = "unit", column = @Column(name = "efficiency_unit"))
    })
    private FuelEfficiency efficiency;

    @Column(name = "previous_mileage")
    private BigDecimal previousMileage;

    /**
     * Private constructor for creating a new fuel record
     */
    private FuelRecord(String id, VehicleId vehicleId, String vehiclePlate, LocalDateTime date,
                      FuelType fuelType, BigDecimal quantity, BigDecimal totalCost,
                      BigDecimal currentMileage, String station, String location,
                      BigDecimal previousMileage) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.vehiclePlate = vehiclePlate;
        this.date = date;
        this.fuelType = fuelType;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.currentMileage = currentMileage;
        this.station = station;
        this.location = location;
        this.previousMileage = previousMileage;
        
        // Calculate efficiency if we have previous mileage
        if (previousMileage != null && currentMileage.compareTo(previousMileage) > 0) {
            BigDecimal distance = currentMileage.subtract(previousMileage);
            this.efficiency = FuelEfficiency.calculate(distance, quantity, "KM/L");
        }
    }

    /**
     * Factory method to create a new fuel record
     */
    public static FuelRecord create(VehicleId vehicleId, String vehiclePlate, LocalDateTime date,
                                  FuelType fuelType, BigDecimal quantity, BigDecimal totalCost,
                                  BigDecimal currentMileage, String station, String location,
                                  BigDecimal previousMileage) {
        
        validateFuelRecordData(vehicleId, vehiclePlate, date, fuelType, quantity, totalCost, currentMileage);
        
        String id = UUID.randomUUID().toString();
        FuelRecord fuelRecord = new FuelRecord(id, vehicleId, vehiclePlate, date, fuelType,
                                             quantity, totalCost, currentMileage, station, location, previousMileage);
        
        // Add domain event
        fuelRecord.addDomainEvent(new FuelRecordCreatedEvent(fuelRecord.getId(), fuelRecord.getVehicleId(), 
                                                           fuelRecord.getEfficiency()));
        
        return fuelRecord;
    }

    /**
     * Update fuel record
     */
    public void update(FuelType fuelType, BigDecimal quantity, BigDecimal totalCost,
                      BigDecimal currentMileage, String station, String location) {
        
        validateFuelRecordData(this.vehicleId, this.vehiclePlate, this.date, fuelType, quantity, totalCost, currentMileage);
        
        this.fuelType = fuelType;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.currentMileage = currentMileage;
        this.station = station;
        this.location = location;
        
        // Recalculate efficiency if we have previous mileage
        if (this.previousMileage != null && currentMileage.compareTo(this.previousMileage) > 0) {
            BigDecimal distance = currentMileage.subtract(this.previousMileage);
            this.efficiency = FuelEfficiency.calculate(distance, quantity, "KM/L");
        }
    }

    /**
     * Get cost per liter
     */
    public BigDecimal getCostPerLiter() {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalCost.divide(quantity, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Get distance traveled
     */
    public BigDecimal getDistanceTraveled() {
        if (previousMileage == null) {
            return BigDecimal.ZERO;
        }
        return currentMileage.subtract(previousMileage);
    }

    /**
     * Check if efficiency is good
     */
    public boolean hasGoodEfficiency() {
        return efficiency != null && efficiency.isGood();
    }

    /**
     * Validate fuel record data
     */
    private static void validateFuelRecordData(VehicleId vehicleId, String vehiclePlate, LocalDateTime date,
                                             FuelType fuelType, BigDecimal quantity, BigDecimal totalCost,
                                             BigDecimal currentMileage) {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (vehiclePlate == null || vehiclePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle plate cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        if (fuelType == null) {
            throw new IllegalArgumentException("Fuel type cannot be null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total cost cannot be negative");
        }
        if (currentMileage == null || currentMileage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current mileage must be positive");
        }
    }

    @Override
    public String getId() {
        return this.id;
    }
} 