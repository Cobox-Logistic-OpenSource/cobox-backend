package coboxlogistic.startup.backend.fleet.domain.services;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.FuelRecord;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetFuelRecordsByDateRangeQuery;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetFuelRecordsByVehicleQuery;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface for fuel record query operations in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public interface FuelRecordQueryService {

    /**
     * Get fuel record by ID
     * @param fuelRecordId the fuel record ID
     * @return the fuel record if found
     */
    Optional<FuelRecord> getFuelRecordById(String fuelRecordId);

    /**
     * Get fuel records by vehicle
     * @param query the get fuel records by vehicle query
     * @return list of fuel records for the vehicle
     */
    List<FuelRecord> getFuelRecordsByVehicle(GetFuelRecordsByVehicleQuery query);

    /**
     * Get fuel records by date range
     * @param query the get fuel records by date range query
     * @return list of fuel records in the date range
     */
    List<FuelRecord> getFuelRecordsByDateRange(GetFuelRecordsByDateRangeQuery query);

    /**
     * Get vehicle fuel statistics
     * @param vehicleId the vehicle ID
     * @return fuel statistics for the vehicle
     */
    FuelStatistics getVehicleFuelStatistics(VehicleId vehicleId);

    /**
     * Fuel statistics record
     */
    record FuelStatistics(
        VehicleId vehicleId,
        java.math.BigDecimal totalFuelConsumed,
        java.math.BigDecimal totalCost,
        java.math.BigDecimal averageEfficiency,
        java.math.BigDecimal totalDistance,
        Long recordCount
    ) {}
} 