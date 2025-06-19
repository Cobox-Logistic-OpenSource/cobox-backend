package coboxlogistic.startup.backend.fleet.domain.model.queries;

import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

/**
 * Query to get mileage records by vehicle in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public record GetMileageRecordsByVehicleQuery(VehicleId vehicleId) {
    
    public GetMileageRecordsByVehicleQuery {
        if (vehicleId == null) {
            throw new IllegalArgumentException("Vehicle ID cannot be null");
        }
    }
} 