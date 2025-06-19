package coboxlogistic.startup.backend.fleet.domain.model.commands;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.MileagePurpose;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Command to create a new mileage record in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record CreateMileageRecordCommand(
    VehicleId vehicleId,
    LocalDateTime date,
    BigDecimal startOdometer,
    BigDecimal endOdometer,
    MileagePurpose purpose,
    String driverId,
    String route,
    String notes
) {
    
    public CreateMileageRecordCommand {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (date.isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        if (startOdometer == null || startOdometer.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Start odometer must be positive");
        }
        if (endOdometer == null || endOdometer.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("End odometer must be positive");
        }
        if (endOdometer.compareTo(startOdometer) <= 0) {
            throw new IllegalArgumentException("End odometer must be greater than start odometer");
        }
        if (purpose == null) {
            throw new IllegalArgumentException("Purpose cannot be null");
        }
    }
} 