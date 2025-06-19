package coboxlogistic.startup.backend.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing a Vehicle License Plate in the Fleet bounded context.
 * Immutable and enforces business rules for license plate format.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LicensePlate implements Serializable {

    private String value;

    // Pattern for license plate validation (adjust based on your country's format)
    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^[A-Z0-9]{3,10}$");

    /**
     * Create a LicensePlate from a string value
     * @param value the license plate string
     * @return new LicensePlate instance
     */
    public static LicensePlate of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }

        String normalizedValue = value.trim().toUpperCase();
        
        if (!LICENSE_PLATE_PATTERN.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException("Invalid license plate format: " + value);
        }

        return new LicensePlate(normalizedValue);
    }

    /**
     * Check if the license plate is valid
     * @param value the license plate to validate
     * @return true if valid
     */
    public static boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return LICENSE_PLATE_PATTERN.matcher(value.trim().toUpperCase()).matches();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LicensePlate that = (LicensePlate) o;
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