package coboxlogistic.startup.backend.fleet.infrastructure.persistence.jpa.repositories;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.FuelRecord;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for FuelRecord aggregate in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Repository
public interface FuelRecordRepository extends JpaRepository<FuelRecord, String> {

    /**
     * Find fuel records by vehicle ID, ordered by date descending
     * @param vehicleId the vehicle ID
     * @return list of fuel records for the vehicle
     */
    List<FuelRecord> findByVehicleIdOrderByDateDesc(VehicleId vehicleId);

    /**
     * Find fuel records by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of fuel records in the date range
     */
    List<FuelRecord> findByDateBetweenOrderByDateDesc(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find fuel records by vehicle plate
     * @param vehiclePlate the vehicle plate
     * @return list of fuel records for the vehicle plate
     */
    List<FuelRecord> findByVehiclePlateOrderByDateDesc(String vehiclePlate);

    /**
     * Find fuel records by fuel type
     * @param fuelType the fuel type
     * @return list of fuel records with the specified fuel type
     */
    List<FuelRecord> findByFuelTypeOrderByDateDesc(coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelType fuelType);

    /**
     * Find fuel records by vehicle ID and date range
     * @param vehicleId the vehicle ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of fuel records for the vehicle in the date range
     */
    @Query("SELECT fr FROM FuelRecord fr WHERE fr.vehicleId = :vehicleId AND fr.date BETWEEN :startDate AND :endDate ORDER BY fr.date DESC")
    List<FuelRecord> findByVehicleIdAndDateBetween(@Param("vehicleId") VehicleId vehicleId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Get total fuel consumption by vehicle
     * @param vehicleId the vehicle ID
     * @return total fuel consumption
     */
    @Query("SELECT SUM(fr.quantity) FROM FuelRecord fr WHERE fr.vehicleId = :vehicleId")
    java.math.BigDecimal getTotalFuelConsumptionByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Get total cost by vehicle
     * @param vehicleId the vehicle ID
     * @return total cost
     */
    @Query("SELECT SUM(fr.totalCost) FROM FuelRecord fr WHERE fr.vehicleId = :vehicleId")
    java.math.BigDecimal getTotalCostByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Get average efficiency by vehicle
     * @param vehicleId the vehicle ID
     * @return average efficiency
     */
    @Query("SELECT AVG(fr.efficiency.value) FROM FuelRecord fr WHERE fr.vehicleId = :vehicleId AND fr.efficiency IS NOT NULL")
    java.math.BigDecimal getAverageEfficiencyByVehicle(@Param("vehicleId") VehicleId vehicleId);

    /**
     * Count fuel records by vehicle
     * @param vehicleId the vehicle ID
     * @return count of fuel records
     */
    long countByVehicleId(VehicleId vehicleId);
} 