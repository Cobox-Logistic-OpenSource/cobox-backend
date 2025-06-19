package coboxlogistic.startup.backend.fleet.domain.model.entities;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.Vehicle;
import coboxlogistic.startup.backend.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Maintenance Record Entity in the Fleet bounded context.
 * Belongs to the Vehicle aggregate and tracks maintenance activities.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Entity
@Table(name = "maintenance_records")
@Getter
@NoArgsConstructor
public class MaintenanceRecord extends AuditableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @Setter
    private Vehicle vehicle;

    @Column(name = "maintenance_type", nullable = false)
    private String maintenanceType;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "mileage_at_maintenance", nullable = false)
    private BigDecimal mileageAtMaintenance;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDateTime maintenanceDate;

    @Column(name = "next_maintenance_mileage")
    private BigDecimal nextMaintenanceMileage;

    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    @Column(name = "is_scheduled")
    private Boolean isScheduled = false;

    /**
     * Private constructor for creating a new maintenance record
     */
    private MaintenanceRecord(String maintenanceType, String description, BigDecimal mileageAtMaintenance,
                            BigDecimal cost, String performedBy, LocalDateTime maintenanceDate,
                            BigDecimal nextMaintenanceMileage, LocalDateTime nextMaintenanceDate, Boolean isScheduled) {
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.mileageAtMaintenance = mileageAtMaintenance;
        this.cost = cost;
        this.performedBy = performedBy;
        this.maintenanceDate = maintenanceDate;
        this.nextMaintenanceMileage = nextMaintenanceMileage;
        this.nextMaintenanceDate = nextMaintenanceDate;
        this.isScheduled = isScheduled;
    }

    /**
     * Factory method to create a new maintenance record
     */
    public static MaintenanceRecord create(String maintenanceType, String description, BigDecimal mileageAtMaintenance,
                                         BigDecimal cost, String performedBy, LocalDateTime maintenanceDate,
                                         BigDecimal nextMaintenanceMileage, LocalDateTime nextMaintenanceDate) {
        
        validateMaintenanceData(maintenanceType, mileageAtMaintenance, maintenanceDate);
        
        return new MaintenanceRecord(maintenanceType, description, mileageAtMaintenance, cost, performedBy,
                                   maintenanceDate, nextMaintenanceMileage, nextMaintenanceDate, false);
    }

    /**
     * Factory method to create a scheduled maintenance record
     */
    public static MaintenanceRecord createScheduled(String maintenanceType, String description, 
                                                   LocalDateTime scheduledDate, BigDecimal scheduledMileage) {
        
        validateMaintenanceData(maintenanceType, scheduledMileage, scheduledDate);
        
        return new MaintenanceRecord(maintenanceType, description, scheduledMileage, null, null,
                                   scheduledDate, null, scheduledDate, true);
    }

    /**
     * Mark maintenance as completed
     */
    public void markAsCompleted(BigDecimal actualMileage, BigDecimal actualCost, String performedBy) {
        if (!this.isScheduled) {
            throw new IllegalStateException("Only scheduled maintenance can be marked as completed");
        }
        
        this.mileageAtMaintenance = actualMileage;
        this.cost = actualCost;
        this.performedBy = performedBy;
        this.maintenanceDate = LocalDateTime.now();
        this.isScheduled = false;
    }

    /**
     * Check if maintenance is overdue
     */
    public boolean isOverdue() {
        if (this.isScheduled) {
            return false;
        }
        
        if (this.nextMaintenanceDate != null && LocalDateTime.now().isAfter(this.nextMaintenanceDate)) {
            return true;
        }
        
        if (this.nextMaintenanceMileage != null && this.vehicle != null) {
            return this.vehicle.getCurrentMileage().compareTo(this.nextMaintenanceMileage) >= 0;
        }
        
        return false;
    }

    /**
     * Check if maintenance is due soon (within 30 days or 1000 km)
     */
    public boolean isDueSoon() {
        if (this.isScheduled) {
            return false;
        }
        
        if (this.nextMaintenanceDate != null) {
            LocalDateTime thirtyDaysFromNow = LocalDateTime.now().plusDays(30);
            if (LocalDateTime.now().isAfter(this.nextMaintenanceDate.minusDays(30))) {
                return true;
            }
        }
        
        if (this.nextMaintenanceMileage != null && this.vehicle != null) {
            BigDecimal currentMileage = this.vehicle.getCurrentMileage();
            BigDecimal threshold = this.nextMaintenanceMileage.subtract(new BigDecimal("1000"));
            return currentMileage.compareTo(threshold) >= 0;
        }
        
        return false;
    }

    /**
     * Get maintenance cost (returns 0 if null)
     */
    public BigDecimal getCost() {
        return this.cost != null ? this.cost : BigDecimal.ZERO;
    }

    /**
     * Check if this is a scheduled maintenance
     */
    public boolean isScheduled() {
        return Boolean.TRUE.equals(this.isScheduled);
    }

    /**
     * Validate maintenance data
     */
    private static void validateMaintenanceData(String maintenanceType, BigDecimal mileage, LocalDateTime date) {
        if (maintenanceType == null || maintenanceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Maintenance type cannot be null or empty");
        }
        if (mileage == null || mileage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Mileage must be positive");
        }
        if (date == null) {
            throw new IllegalArgumentException("Maintenance date cannot be null");
        }
        if (date.isAfter(LocalDateTime.now().plusDays(1))) {
            throw new IllegalArgumentException("Maintenance date cannot be in the future");
        }
    }
} 