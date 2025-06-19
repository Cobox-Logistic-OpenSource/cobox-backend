package coboxlogistic.startup.backend.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing a Vehicle ID in the Fleet bounded context.
 * Immutable and provides business identity for vehicles.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleId implements Serializable {

    private String value;

    /**
     * Create a new VehicleId with a generated UUID
     * @return new VehicleId instance
     */
    public static VehicleId generate() {
        return new VehicleId(UUID.randomUUID().toString());
    }

    /**
     * Create a VehicleId from a string value
     * @param value the string value
     * @return new VehicleId instance
     */
    public static VehicleId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or empty");
        }
        return new VehicleId(value.trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleId vehicleId = (VehicleId) o;
        return Objects.equals(value, vehicleId.value);
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