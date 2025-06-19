package coboxlogistic.startup.backend.fleet.domain.model.aggregates;

import coboxlogistic.startup.backend.fleet.domain.model.entities.MaintenanceRecord;
import coboxlogistic.startup.backend.fleet.domain.model.events.VehicleRegisteredEvent;
import coboxlogistic.startup.backend.fleet.domain.model.events.VehicleStatusChangedEvent;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.*;
import coboxlogistic.startup.backend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Vehicle Aggregate Root in the Fleet bounded context.
 * Manages vehicle lifecycle, status transitions, and maintenance records.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Entity
@Table(name = "vehicles")
@Getter
@NoArgsConstructor
public class Vehicle extends AuditableAbstractAggregateRoot<VehicleId> {

    @EmbeddedId
    private VehicleId id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "license_plate", unique = true, nullable = false))
    })
    private LicensePlate licensePlate;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "fuel_type", nullable = false))
    })
    private FuelType fuelType;

    @Column(name = "engine_capacity", precision = 5, scale = 2)
    private BigDecimal engineCapacity;

    @Column(name = "current_mileage", nullable = false)
    private BigDecimal currentMileage;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "value", column = @Column(name = "status", nullable = false))
    })
    private VehicleStatus status;

    @Column(name = "color")
    private String color;

    @Column(name = "vin", unique = true)
    private String vin;

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    /**
     * Private constructor for creating a new vehicle
     */
    private Vehicle(VehicleId id, LicensePlate licensePlate, String brand, String model, 
                   Integer year, FuelType fuelType, BigDecimal engineCapacity, 
                   BigDecimal currentMileage, String color, String vin, String description) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.fuelType = fuelType;
        this.engineCapacity = engineCapacity;
        this.currentMileage = currentMileage;
        this.status = VehicleStatus.PENDING_REGISTRATION;
        this.color = color;
        this.vin = vin;
        this.description = description;
    }

    /**
     * Factory method to create a new vehicle
     */
    public static Vehicle create(LicensePlate licensePlate, String brand, String model, 
                               Integer year, FuelType fuelType, BigDecimal engineCapacity, 
                               BigDecimal currentMileage, String color, String vin, String description) {
        
        validateVehicleData(licensePlate, brand, model, year, fuelType, currentMileage);
        
        VehicleId id = VehicleId.generate();
        Vehicle vehicle = new Vehicle(id, licensePlate, brand, model, year, fuelType, 
                                    engineCapacity, currentMileage, color, vin, description);
        
        // Add domain event
        vehicle.addDomainEvent(new VehicleRegisteredEvent(vehicle.getId(), vehicle.getLicensePlate()));
        
        return vehicle;
    }

    /**
     * Activate the vehicle
     */
    public void activate() {
        if (!VehicleStatus.PENDING_REGISTRATION.equals(this.status)) {
            throw new IllegalStateException("Vehicle must be in PENDING_REGISTRATION status to be activated");
        }
        
        this.status = VehicleStatus.ACTIVE;
        addDomainEvent(new VehicleStatusChangedEvent(this.id, VehicleStatus.PENDING_REGISTRATION, this.status));
    }

    /**
     * Put vehicle in maintenance
     */
    public void putInMaintenance() {
        if (!this.status.isOperational()) {
            throw new IllegalStateException("Vehicle must be operational to be put in maintenance");
        }
        
        VehicleStatus previousStatus = this.status;
        this.status = VehicleStatus.MAINTENANCE;
        addDomainEvent(new VehicleStatusChangedEvent(this.id, previousStatus, this.status));
    }

    /**
     * Take vehicle out of service
     */
    public void takeOutOfService() {
        if (VehicleStatus.RETIRED.equals(this.status)) {
            throw new IllegalStateException("Retired vehicle cannot be taken out of service");
        }
        
        VehicleStatus previousStatus = this.status;
        this.status = VehicleStatus.OUT_OF_SERVICE;
        addDomainEvent(new VehicleStatusChangedEvent(this.id, previousStatus, this.status));
    }

    /**
     * Reactivate vehicle
     */
    public void reactivate() {
        if (!VehicleStatus.MAINTENANCE.equals(this.status) && !VehicleStatus.OUT_OF_SERVICE.equals(this.status)) {
            throw new IllegalStateException("Vehicle must be in maintenance or out of service to be reactivated");
        }
        
        VehicleStatus previousStatus = this.status;
        this.status = VehicleStatus.ACTIVE;
        addDomainEvent(new VehicleStatusChangedEvent(this.id, previousStatus, this.status));
    }

    /**
     * Retire vehicle
     */
    public void retire() {
        if (!this.status.canBeRetired()) {
            throw new IllegalStateException("Vehicle cannot be retired in current status: " + this.status);
        }
        
        VehicleStatus previousStatus = this.status;
        this.status = VehicleStatus.RETIRED;
        addDomainEvent(new VehicleStatusChangedEvent(this.id, previousStatus, this.status));
    }

    /**
     * Update vehicle mileage
     */
    public void updateMileage(BigDecimal newMileage) {
        if (newMileage == null || newMileage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Mileage must be positive");
        }
        
        if (newMileage.compareTo(this.currentMileage) < 0) {
            throw new IllegalArgumentException("New mileage cannot be less than current mileage");
        }
        
        this.currentMileage = newMileage;
    }

    /**
     * Add maintenance record
     */
    public void addMaintenanceRecord(MaintenanceRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Maintenance record cannot be null");
        }
        
        record.setVehicle(this);
        this.maintenanceRecords.add(record);
    }

    /**
     * Get maintenance records (immutable)
     */
    public List<MaintenanceRecord> getMaintenanceRecords() {
        return Collections.unmodifiableList(maintenanceRecords);
    }

    /**
     * Check if vehicle is operational
     */
    public boolean isOperational() {
        return this.status.isOperational();
    }

    /**
     * Check if vehicle needs maintenance
     */
    public boolean needsMaintenance() {
        // Simple logic: if mileage is high or status is maintenance
        return VehicleStatus.MAINTENANCE.equals(this.status) || 
               this.currentMileage.compareTo(new BigDecimal("100000")) > 0;
    }

    /**
     * Validate vehicle data
     */
    private static void validateVehicleData(LicensePlate licensePlate, String brand, String model, 
                                          Integer year, FuelType fuelType, BigDecimal currentMileage) {
        if (licensePlate == null) {
            throw new IllegalArgumentException("License plate cannot be null");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (year == null || year < 1900 || year > LocalDateTime.now().getYear() + 1) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        if (fuelType == null) {
            throw new IllegalArgumentException("Fuel type cannot be null");
        }
        if (currentMileage == null || currentMileage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current mileage must be positive");
        }
    }

    @Override
    public VehicleId getId() {
        return this.id;
    }
} 