package coboxlogistic.startup.backend.fleet.domain.model.commands;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelType;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.LicensePlate;

import java.math.BigDecimal;

/**
 * Command to register a new vehicle in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record RegisterVehicleCommand(
    LicensePlate licensePlate,
    String brand,
    String model,
    Integer year,
    FuelType fuelType,
    BigDecimal engineCapacity,
    BigDecimal currentMileage,
    String color,
    String vin,
    String description
) {
    
    public RegisterVehicleCommand {
        if (licensePlate == null) {
            throw new IllegalArgumentException("License plate cannot be null");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (year == null || year < 1900 || year > java.time.LocalDateTime.now().getYear() + 1) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        if (fuelType == null) {
            throw new IllegalArgumentException("Fuel type cannot be null");
        }
        if (currentMileage == null || currentMileage.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current mileage must be positive");
        }
    }
} 