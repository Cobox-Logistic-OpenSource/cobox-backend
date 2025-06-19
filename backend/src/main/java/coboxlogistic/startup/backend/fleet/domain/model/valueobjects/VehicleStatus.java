package coboxlogistic.startup.backend.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object representing a Vehicle Status in the Fleet bounded context.
 * Immutable and defines the possible states a vehicle can be in.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleStatus implements Serializable {

    private String value;

    // Predefined status constants
    public static final VehicleStatus ACTIVE = new VehicleStatus("ACTIVE");
    public static final VehicleStatus MAINTENANCE = new VehicleStatus("MAINTENANCE");
    public static final VehicleStatus OUT_OF_SERVICE = new VehicleStatus("OUT_OF_SERVICE");
    public static final VehicleStatus RETIRED = new VehicleStatus("RETIRED");
    public static final VehicleStatus PENDING_REGISTRATION = new VehicleStatus("PENDING_REGISTRATION");

    /**
     * Create a VehicleStatus from a string value
     * @param value the status string
     * @return new VehicleStatus instance
     */
    public static VehicleStatus of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle status cannot be null or empty");
        }

        String normalizedValue = value.trim().toUpperCase();
        
        // Validate against allowed values
        if (!isValid(normalizedValue)) {
            throw new IllegalArgumentException("Invalid vehicle status: " + value);
        }

        return new VehicleStatus(normalizedValue);
    }

    /**
     * Check if the status is valid
     * @param value the status to validate
     * @return true if valid
     */
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        String normalizedValue = value.trim().toUpperCase();
        return normalizedValue.equals("ACTIVE") ||
               normalizedValue.equals("MAINTENANCE") ||
               normalizedValue.equals("OUT_OF_SERVICE") ||
               normalizedValue.equals("RETIRED") ||
               normalizedValue.equals("PENDING_REGISTRATION");
    }

    /**
     * Check if the vehicle can be used for operations
     * @return true if the vehicle is operational
     */
    public boolean isOperational() {
        return ACTIVE.equals(this);
    }

    /**
     * Check if the vehicle is available for maintenance
     * @return true if the vehicle can be maintained
     */
    public boolean canBeMaintained() {
        return ACTIVE.equals(this) || MAINTENANCE.equals(this);
    }

    /**
     * Check if the vehicle can be retired
     * @return true if the vehicle can be retired
     */
    public boolean canBeRetired() {
        return !RETIRED.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleStatus that = (VehicleStatus) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 