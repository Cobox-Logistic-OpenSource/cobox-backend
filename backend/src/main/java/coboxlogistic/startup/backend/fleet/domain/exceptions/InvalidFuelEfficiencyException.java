package coboxlogistic.startup.backend.fleet.domain.exceptions;

/**
 * Domain Exception thrown when fuel efficiency is invalid in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public class InvalidFuelEfficiencyException extends RuntimeException {

    public InvalidFuelEfficiencyException(String message) {
        super(message);
    }

    public InvalidFuelEfficiencyException(String message, Throwable cause) {
        super(message, cause);
    }
} 