package coboxlogistic.startup.backend.fleet.domain.model.queries;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleStatus;

/**
 * Query to get vehicles by status in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record GetVehiclesByStatusQuery(VehicleStatus status) {
    
    public GetVehiclesByStatusQuery {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }
} 