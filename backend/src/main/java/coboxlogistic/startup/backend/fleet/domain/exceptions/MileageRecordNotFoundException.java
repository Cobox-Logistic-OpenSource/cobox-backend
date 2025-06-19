package coboxlogistic.startup.backend.fleet.domain.exceptions;

/**
 * Domain Exception thrown when a mileage record is not found in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class MileageRecordNotFoundException extends RuntimeException {

    private final String mileageRecordId;

    public MileageRecordNotFoundException(String mileageRecordId) {
        super("Mileage record not found with ID: " + mileageRecordId);
        this.mileageRecordId = mileageRecordId;
    }

    public MileageRecordNotFoundException(String message, String mileageRecordId) {
        super(message);
        this.mileageRecordId = mileageRecordId;
    }

    public MileageRecordNotFoundException(String message, String mileageRecordId, Throwable cause) {
        super(message, cause);
        this.mileageRecordId = mileageRecordId;
    }

    public String getMileageRecordId() {
        return mileageRecordId;
    }
} 