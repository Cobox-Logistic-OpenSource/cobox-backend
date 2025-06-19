package coboxlogistic.startup.backend.fleet.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing Fuel Efficiency in the Fleet bounded context.
 * Immutable and provides calculated fuel efficiency metrics.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuelEfficiency implements Serializable {

    private BigDecimal value; // km/L or km/kWh
    private String unit; // "km/L" for fuel, "km/kWh" for electric

    // Efficiency thresholds
    private static final BigDecimal MIN_EFFICIENCY = new BigDecimal("0.1");
    private static final BigDecimal MAX_EFFICIENCY = new BigDecimal("50.0");

    /**
     * Create a FuelEfficiency from a value and unit
     * @param value the efficiency value
     * @param unit the unit of measurement
     * @return new FuelEfficiency instance
     */
    public static FuelEfficiency of(BigDecimal value, String unit) {
        if (value == null) {
            throw new IllegalArgumentException("Efficiency value cannot be null");
        }
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Efficiency unit cannot be null or empty");
        }

        // Validate efficiency range
        if (value.compareTo(MIN_EFFICIENCY) < 0 || value.compareTo(MAX_EFFICIENCY) > 0) {
            throw new IllegalArgumentException("Efficiency value must be between " + MIN_EFFICIENCY + " and " + MAX_EFFICIENCY);
        }

        // Validate unit
        String normalizedUnit = unit.trim().toUpperCase();
        if (!isValidUnit(normalizedUnit)) {
            throw new IllegalArgumentException("Invalid efficiency unit: " + unit);
        }

        return new FuelEfficiency(value.setScale(2, RoundingMode.HALF_UP), normalizedUnit);
    }

    /**
     * Create a FuelEfficiency for fuel consumption (km/L)
     * @param value the efficiency value in km/L
     * @return new FuelEfficiency instance
     */
    public static FuelEfficiency forFuel(BigDecimal value) {
        return of(value, "KM/L");
    }

    /**
     * Create a FuelEfficiency for electric consumption (km/kWh)
     * @param value the efficiency value in km/kWh
     * @return new FuelEfficiency instance
     */
    public static FuelEfficiency forElectric(BigDecimal value) {
        return of(value, "KM/KWH");
    }

    /**
     * Calculate efficiency from distance and consumption
     * @param distanceKm the distance in kilometers
     * @param consumption the consumption amount
     * @param unit the unit of consumption
     * @return calculated FuelEfficiency
     */
    public static FuelEfficiency calculate(BigDecimal distanceKm, BigDecimal consumption, String unit) {
        if (distanceKm == null || distanceKm.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Distance must be positive");
        }
        if (consumption == null || consumption.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Consumption must be positive");
        }

        BigDecimal efficiency = distanceKm.divide(consumption, 2, RoundingMode.HALF_UP);
        return of(efficiency, unit);
    }

    /**
     * Check if the unit is valid
     * @param unit the unit to validate
     * @return true if valid
     */
    public static boolean isValidUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            return false;
        }
        
        String normalizedUnit = unit.trim().toUpperCase();
        return normalizedUnit.equals("KM/L") || 
               normalizedUnit.equals("KM/KWH") ||
               normalizedUnit.equals("MPG") ||
               normalizedUnit.equals("L/100KM");
    }

    /**
     * Check if the efficiency is good (above threshold)
     * @return true if efficiency is good
     */
    public boolean isGood() {
        BigDecimal threshold = "KM/L".equals(unit) ? new BigDecimal("10.0") : new BigDecimal("5.0");
        return value.compareTo(threshold) >= 0;
    }

    /**
     * Check if the efficiency is poor (below threshold)
     * @return true if efficiency is poor
     */
    public boolean isPoor() {
        BigDecimal threshold = "KM/L".equals(unit) ? new BigDecimal("5.0") : new BigDecimal("2.0");
        return value.compareTo(threshold) < 0;
    }

    /**
     * Convert to different unit
     * @param targetUnit the target unit
     * @return converted FuelEfficiency
     */
    public FuelEfficiency convertTo(String targetUnit) {
        if (!isValidUnit(targetUnit)) {
            throw new IllegalArgumentException("Invalid target unit: " + targetUnit);
        }

        String normalizedTargetUnit = targetUnit.trim().toUpperCase();
        if (normalizedTargetUnit.equals(unit)) {
            return this;
        }

        BigDecimal convertedValue = value;
        
        // Conversion logic (simplified)
        if ("KM/L".equals(unit) && "MPG".equals(normalizedTargetUnit)) {
            convertedValue = value.multiply(new BigDecimal("2.352"));
        } else if ("MPG".equals(unit) && "KM/L".equals(normalizedTargetUnit)) {
            convertedValue = value.divide(new BigDecimal("2.352"), 2, RoundingMode.HALF_UP);
        } else if ("KM/L".equals(unit) && "L/100KM".equals(normalizedTargetUnit)) {
            convertedValue = new BigDecimal("100").divide(value, 2, RoundingMode.HALF_UP);
        } else if ("L/100KM".equals(unit) && "KM/L".equals(normalizedTargetUnit)) {
            convertedValue = new BigDecimal("100").divide(value, 2, RoundingMode.HALF_UP);
        }

        return new FuelEfficiency(convertedValue, normalizedTargetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuelEfficiency that = (FuelEfficiency) o;
        return Objects.equals(value, that.value) && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
} 