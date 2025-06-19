package coboxlogistic.startup.backend.fleet.infrastructure.persistence.jpa.repositories;

import coboxlogistic.startup.backend.fleet.domain.model.entities.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Repository for MaintenanceRecord entity in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    /**
     * Find maintenance records by vehicle ID, ordered by date descending
     * @param vehicleId the vehicle ID
     * @return list of maintenance records for the vehicle
     */
    @Query("SELECT mr FROM MaintenanceRecord mr WHERE mr.vehicle.id = :vehicleId ORDER BY mr.maintenanceDate DESC")
    List<MaintenanceRecord> findByVehicleIdOrderByMaintenanceDateDesc(@Param("vehicleId") String vehicleId);

    /**
     * Find maintenance records by maintenance type
     * @param maintenanceType the maintenance type
     * @return list of maintenance records with the specified type
     */
    List<MaintenanceRecord> findByMaintenanceTypeOrderByMaintenanceDateDesc(String maintenanceType);

    /**
     * Find scheduled maintenance records
     * @return list of scheduled maintenance records
     */
    List<MaintenanceRecord> findByIsScheduledTrueOrderByMaintenanceDateAsc();

    /**
     * Find overdue maintenance records
     * @return list of overdue maintenance records
     */
    @Query("SELECT mr FROM MaintenanceRecord mr WHERE mr.isScheduled = false AND " +
           "(mr.nextMaintenanceDate < :now OR " +
           "(mr.nextMaintenanceMileage IS NOT NULL AND mr.vehicle.currentMileage >= mr.nextMaintenanceMileage))")
    List<MaintenanceRecord> findOverdueMaintenanceRecords(@Param("now") LocalDateTime now);

    /**
     * Find maintenance records due soon (within 30 days)
     * @return list of maintenance records due soon
     */
    @Query("SELECT mr FROM MaintenanceRecord mr WHERE mr.isScheduled = false AND " +
           "mr.nextMaintenanceDate BETWEEN :now AND :thirtyDaysFromNow")
    List<MaintenanceRecord> findMaintenanceRecordsDueSoon(@Param("now") LocalDateTime now,
                                                         @Param("thirtyDaysFromNow") LocalDateTime thirtyDaysFromNow);

    /**
     * Get total maintenance cost by vehicle
     * @param vehicleId the vehicle ID
     * @return total maintenance cost
     */
    @Query("SELECT SUM(mr.cost) FROM MaintenanceRecord mr WHERE mr.vehicle.id = :vehicleId AND mr.isScheduled = false")
    java.math.BigDecimal getTotalMaintenanceCostByVehicle(@Param("vehicleId") String vehicleId);

    /**
     * Count maintenance records by vehicle
     * @param vehicleId the vehicle ID
     * @return count of maintenance records
     */
    @Query("SELECT COUNT(mr) FROM MaintenanceRecord mr WHERE mr.vehicle.id = :vehicleId")
    long countByVehicleId(@Param("vehicleId") String vehicleId);
} 