package coboxlogistic.startup.backend.fleet.domain.services;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.Vehicle;
import coboxlogistic.startup.backend.fleet.domain.model.commands.RegisterVehicleCommand;
import coboxlogistic.startup.backend.fleet.domain.model.commands.UpdateVehicleStatusCommand;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

/**
 * Domain service interface for vehicle command operations in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public interface VehicleCommandService {

    /**
     * Register a new vehicle
     * @param command the register vehicle command
     * @return the created vehicle
     */
    Vehicle registerVehicle(RegisterVehicleCommand command);

    /**
     * Update vehicle status
     * @param command the update vehicle status command
     * @return the updated vehicle
     */
    Vehicle updateVehicleStatus(UpdateVehicleStatusCommand command);

    /**
     * Delete a vehicle
     * @param vehicleId the vehicle ID
     */
    void deleteVehicle(VehicleId vehicleId);

    /**
     * Update vehicle mileage
     * @param vehicleId the vehicle ID
     * @param newMileage the new mileage
     * @return the updated vehicle
     */
    Vehicle updateVehicleMileage(VehicleId vehicleId, java.math.BigDecimal newMileage);
} 