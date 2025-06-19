package coboxlogistic.startup.backend.fleet.domain.model.queries;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

/**
 * Query to get fuel records by vehicle in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record GetFuelRecordsByVehicleQuery(VehicleId vehicleId) {
    
    public GetFuelRecordsByVehicleQuery {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
    }
} 