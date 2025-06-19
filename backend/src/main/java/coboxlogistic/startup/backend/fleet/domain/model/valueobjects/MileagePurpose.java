package coboxlogistic.startup.backend.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object representing a Mileage Purpose in the Fleet bounded context.
 * Immutable and defines the possible purposes for mileage records.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MileagePurpose implements Serializable {

    private String value;

    // Predefined mileage purpose constants
    public static final MileagePurpose DELIVERY = new MileagePurpose("DELIVERY");
    public static final MileagePurpose PICKUP = new MileagePurpose("PICKUP");
    public static final MileagePurpose MAINTENANCE = new MileagePurpose("MAINTENANCE");
    public static final MileagePurpose PERSONAL = new MileagePurpose("PERSONAL");
    public static final MileagePurpose RELOCATION = new MileagePurpose("RELOCATION");
    public static final MileagePurpose TRAINING = new MileagePurpose("TRAINING");
    public static final MileagePurpose TESTING = new MileagePurpose("TESTING");

    /**
     * Create a MileagePurpose from a string value
     * @param value the purpose string
     * @return new MileagePurpose instance
     */
    public static MileagePurpose of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Mileage purpose cannot be null or empty");
        }

        String normalizedValue = value.trim().toUpperCase();
        
        // Validate against allowed values
        if (!isValid(normalizedValue)) {
            throw new IllegalArgumentException("Invalid mileage purpose: " + value);
        }

        return new MileagePurpose(normalizedValue);
    }

    /**
     * Check if the purpose is valid
     * @param value the purpose to validate
     * @return true if valid
     */
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        String normalizedValue = value.trim().toUpperCase();
        return normalizedValue.equals("DELIVERY") ||
               normalizedValue.equals("PICKUP") ||
               normalizedValue.equals("MAINTENANCE") ||
               normalizedValue.equals("PERSONAL") ||
               normalizedValue.equals("RELOCATION") ||
               normalizedValue.equals("TRAINING") ||
               normalizedValue.equals("TESTING");
    }

    /**
     * Check if the purpose is business related
     * @return true if it's business related
     */
    public boolean isBusinessRelated() {
        return DELIVERY.equals(this) || 
               PICKUP.equals(this) || 
               MAINTENANCE.equals(this) || 
               RELOCATION.equals(this) || 
               TRAINING.equals(this) || 
               TESTING.equals(this);
    }

    /**
     * Check if the purpose is operational
     * @return true if it's operational
     */
    public boolean isOperational() {
        return DELIVERY.equals(this) || PICKUP.equals(this);
    }

    /**
     * Check if the purpose is maintenance related
     * @return true if it's maintenance related
     */
    public boolean isMaintenanceRelated() {
        return MAINTENANCE.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MileagePurpose that = (MileagePurpose) o;
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