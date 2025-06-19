package coboxlogistic.startup.backend.fleet.domain.exceptions;

/**
 * Domain Exception thrown when a fuel record is not found in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class FuelRecordNotFoundException extends RuntimeException {

    private final String fuelRecordId;

    public FuelRecordNotFoundException(String fuelRecordId) {
        super("Fuel record not found with ID: " + fuelRecordId);
        this.fuelRecordId = fuelRecordId;
    }

    public FuelRecordNotFoundException(String message, String fuelRecordId) {
        super(message);
        this.fuelRecordId = fuelRecordId;
    }

    public FuelRecordNotFoundException(String message, String fuelRecordId, Throwable cause) {
        super(message, cause);
        this.fuelRecordId = fuelRecordId;
    }

    public String getFuelRecordId() {
        return fuelRecordId;
    }
} 