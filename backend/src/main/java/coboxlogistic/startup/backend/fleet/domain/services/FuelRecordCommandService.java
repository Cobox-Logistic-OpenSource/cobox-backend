package coboxlogistic.startup.backend.fleet.domain.services;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.FuelRecord;
import coboxlogistic.startup.backend.fleet.domain.model.commands.CreateFuelRecordCommand;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelType;

import java.math.BigDecimal;

/**
 * Domain service interface for fuel record command operations in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public interface FuelRecordCommandService {

    /**
     * Create a new fuel record
     * @param command the create fuel record command
     * @return the created fuel record
     */
    FuelRecord createFuelRecord(CreateFuelRecordCommand command);

    /**
     * Update a fuel record
     * @param fuelRecordId the fuel record ID
     * @param fuelType the fuel type
     * @param quantity the quantity
     * @param totalCost the total cost
     * @param currentMileage the current mileage
     * @param station the station
     * @param location the location
     * @return the updated fuel record
     */
    FuelRecord updateFuelRecord(String fuelRecordId, FuelType fuelType, BigDecimal quantity,
                               BigDecimal totalCost, BigDecimal currentMileage, String station, String location);

    /**
     * Delete a fuel record
     * @param fuelRecordId the fuel record ID
     */
    void deleteFuelRecord(String fuelRecordId);
} 