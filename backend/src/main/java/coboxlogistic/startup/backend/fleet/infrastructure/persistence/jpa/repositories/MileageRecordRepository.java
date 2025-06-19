package coboxlogistic.startup.backend.fleet.infrastructure.persistence.jpa.repositories;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.MileageRecord;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for MileageRecord aggregate in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Repository
public interface MileageRecordRepository extends JpaRepository<MileageRecord, String> {

    /**
     * Find mileage records by vehicle ID, ordered by date descending
     * @param vehicleId the vehicle ID
     * @return list of mileage records for the vehicle
     */
    List<MileageRecord> findByVehicleIdOrderByDateDesc(VehicleId vehicleId);

    /**
     * Find mileage records by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of mileage records in the date range
     */
    List<MileageRecord> findByDateBetweenOrderByDateDesc(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find mileage records by purpose
     * @param purpose the mileage purpose
     * @return list of mileage records with the specified purpose
     */
    List<MileageRecord> findByPurposeOrderByDateDesc(coboxlogistic.startup.backend.fleet.domain.model.valueobjects.MileagePurpose purpose);

    /**
     * Find mileage records by driver ID
     * @param driverId the driver ID
     * @return list of mileage records for the driver
     */
    List<MileageRecord> findByDriverIdOrderByDateDesc(String driverId);

    /**
     * Find mileage records by vehicle ID and date range
     * @param vehicleId the vehicle ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of mileage records for the vehicle in the date range
     */
    @Query("SELECT mr FROM MileageRecord mr WHERE mr.vehicleId = :vehicleId AND mr.date BETWEEN :startDate AND :endDate ORDER BY mr.date DESC")
    List<MileageRecord> findByVehicleIdAndDateBetween(@Param("vehicleId") VehicleId vehicleId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Get total mileage by vehicle
     * @param vehicleId the vehicle ID
     * @return total mileage
     */
    @Query("SELECT SUM(mr.distance) FROM MileageRecord mr WHERE mr.vehicleId = :vehicleId")
    java.math.BigDecimal getTotalMileageByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Get total business mileage by vehicle
     * @param vehicleId the vehicle ID
     * @return total business mileage
     */
    @Query("SELECT SUM(mr.distance) FROM MileageRecord mr WHERE mr.vehicleId = :vehicleId AND mr.purpose.value IN ('DELIVERY', 'PICKUP', 'MAINTENANCE', 'RELOCATION', 'TRAINING', 'TESTING')")
    java.math.BigDecimal getTotalBusinessMileageByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Get total personal mileage by vehicle
     * @param vehicleId the vehicle ID
     * @return total personal mileage
     */
    @Query("SELECT SUM(mr.distance) FROM MileageRecord mr WHERE mr.vehicleId = :vehicleId AND mr.purpose.value = 'PERSONAL'")
    java.math.BigDecimal getTotalPersonalMileageByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Get total maintenance mileage by vehicle
     * @param vehicleId the vehicle ID
     * @return total maintenance mileage
     */
    @Query("SELECT SUM(mr.distance) FROM MileageRecord mr WHERE mr.vehicleId = :vehicleId AND mr.purpose.value = 'MAINTENANCE'")
    java.math.BigDecimal getTotalMaintenanceMileageByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Count mileage records by vehicle
     * @param vehicleId the vehicle ID
     * @return count of mileage records
     */
    long countByVehicleId(VehicleId vehicleId);
} 