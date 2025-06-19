package coboxlogistic.startup.backend.fleet.domain.services;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.Vehicle;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetAllVehiclesQuery;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetVehicleByIdQuery;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetVehiclesByStatusQuery;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface for vehicle query operations in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public interface VehicleQueryService {

    /**
     * Get vehicle by ID
     * @param query the get vehicle by ID query
     * @return the vehicle if found
     */
    Optional<Vehicle> getVehicleById(GetVehicleByIdQuery query);

    /**
     * Get all vehicles
     * @param query the get all vehicles query
     * @return list of all vehicles
     */
    List<Vehicle> getAllVehicles(GetAllVehiclesQuery query);

    /**
     * Get vehicles by status
     * @param query the get vehicles by status query
     * @return list of vehicles with the specified status
     */
    List<Vehicle> getVehiclesByStatus(GetVehiclesByStatusQuery query);

    /**
     * Check if vehicle exists
     * @param vehicleId the vehicle ID
     * @return true if vehicle exists
     */
    boolean vehicleExists(VehicleId vehicleId);
} 