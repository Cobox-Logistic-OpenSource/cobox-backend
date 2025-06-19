package coboxlogistic.startup.backend.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value Object representing a Fuel Type in the Fleet bounded context.
 * Immutable and defines the possible fuel types for vehicles.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuelType implements Serializable {

    private String value;

    // Predefined fuel type constants
    public static final FuelType GASOLINE = new FuelType("GASOLINE");
    public static final FuelType DIESEL = new FuelType("DIESEL");
    public static final FuelType ELECTRIC = new FuelType("ELECTRIC");
    public static final FuelType HYBRID = new FuelType("HYBRID");
    public static final FuelType LPG = new FuelType("LPG");
    public static final FuelType CNG = new FuelType("CNG");

    /**
     * Create a FuelType from a string value
     * @param value the fuel type string
     * @return new FuelType instance
     */
    public static FuelType of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Fuel type cannot be null or empty");
        }

        String normalizedValue = value.trim().toUpperCase();
        
        // Validate against allowed values
        if (!isValid(normalizedValue)) {
            throw new IllegalArgumentException("Invalid fuel type: " + value);
        }

        return new FuelType(normalizedValue);
    }

    /**
     * Check if the fuel type is valid
     * @param value the fuel type to validate
     * @return true if valid
     */
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        String normalizedValue = value.trim().toUpperCase();
        return normalizedValue.equals("GASOLINE") ||
               normalizedValue.equals("DIESEL") ||
               normalizedValue.equals("ELECTRIC") ||
               normalizedValue.equals("HYBRID") ||
               normalizedValue.equals("LPG") ||
               normalizedValue.equals("CNG");
    }

    /**
     * Check if the fuel type is fossil fuel
     * @return true if it's a fossil fuel
     */
    public boolean isFossilFuel() {
        return GASOLINE.equals(this) || DIESEL.equals(this) || LPG.equals(this) || CNG.equals(this);
    }

    /**
     * Check if the fuel type is electric
     * @return true if it's electric
     */
    public boolean isElectric() {
        return ELECTRIC.equals(this) || HYBRID.equals(this);
    }

    /**
     * Check if the fuel type is hybrid
     * @return true if it's hybrid
     */
    public boolean isHybrid() {
        return HYBRID.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelType fuelType = (FuelType) o;
        return Objects.equals(value, fuelType.value);
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