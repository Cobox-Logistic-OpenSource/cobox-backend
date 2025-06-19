package coboxlogistic.startup.backend.fleet.domain.model.commands;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelType;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Command to create a new fuel record in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record CreateFuelRecordCommand(
    VehicleId vehicleId,
    String vehiclePlate,
    LocalDateTime date,
    FuelType fuelType,
    BigDecimal quantity,
    BigDecimal totalCost,
    BigDecimal currentMileage,
    String station,
    String location,
    BigDecimal previousMileage
) {
    
    public CreateFuelRecordCommand {
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
} 