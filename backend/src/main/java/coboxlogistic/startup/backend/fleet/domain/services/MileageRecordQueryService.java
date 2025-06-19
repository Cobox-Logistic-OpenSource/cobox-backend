package coboxlogistic.startup.backend.fleet.domain.services;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.MileageRecord;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetMileageRecordsByDateRangeQuery;
import coboxlogistic.startup.backend.fleet.domain.model.queries.GetMileageRecordsByVehicleQuery;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface for mileage record query operations in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public interface MileageRecordQueryService {

    /**
     * Get mileage record by ID
     * @param mileageRecordId the mileage record ID
     * @return the mileage record if found
     */
    Optional<MileageRecord> getMileageRecordById(String mileageRecordId);

    /**
     * Get mileage records by vehicle
     * @param query the get mileage records by vehicle query
     * @return list of mileage records for the vehicle
     */
    List<MileageRecord> getMileageRecordsByVehicle(GetMileageRecordsByVehicleQuery query);

    /**
     * Get mileage records by date range
     * @param query the get mileage records by date range query
     * @return list of mileage records in the date range
     */
    List<MileageRecord> getMileageRecordsByDateRange(GetMileageRecordsByDateRangeQuery query);

    /**
     * Get vehicle total mileage
     * @param vehicleId the vehicle ID
     * @return total mileage for the vehicle
     */
    java.math.BigDecimal getVehicleTotalMileage(VehicleId vehicleId);

    /**
     * Get vehicle mileage statistics
     * @param vehicleId the vehicle ID
     * @return mileage statistics for the vehicle
     */
    MileageStatistics getVehicleMileageStatistics(VehicleId vehicleId);

    /**
     * Mileage statistics record
     */
    record MileageStatistics(
        VehicleId vehicleId,
        java.math.BigDecimal totalMileage,
        java.math.BigDecimal businessMileage,
        java.math.BigDecimal personalMileage,
        java.math.BigDecimal maintenanceMileage,
        Long recordCount
    ) {}
} 