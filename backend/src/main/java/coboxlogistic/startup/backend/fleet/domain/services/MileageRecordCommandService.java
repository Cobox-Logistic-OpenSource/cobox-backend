package coboxlogistic.startup.backend.fleet.domain.services;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.MileageRecord;
import coboxlogistic.startup.backend.fleet.domain.model.commands.CreateMileageRecordCommand;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.MileagePurpose;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain service interface for mileage record command operations in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
public interface MileageRecordCommandService {

    /**
     * Create a new mileage record
     * @param command the create mileage record command
     * @return the created mileage record
     */
    MileageRecord createMileageRecord(CreateMileageRecordCommand command);

    /**
     * Update a mileage record
     * @param mileageRecordId the mileage record ID
     * @param date the date
     * @param startOdometer the start odometer
     * @param endOdometer the end odometer
     * @param purpose the purpose
     * @param driverId the driver ID
     * @param route the route
     * @param notes the notes
     * @return the updated mileage record
     */
    MileageRecord updateMileageRecord(String mileageRecordId, LocalDateTime date, BigDecimal startOdometer,
                                     BigDecimal endOdometer, MileagePurpose purpose, String driverId, 
                                     String route, String notes);

    /**
     * Delete a mileage record
     * @param mileageRecordId the mileage record ID
     */
    void deleteMileageRecord(String mileageRecordId);
} 