package coboxlogistic.startup.backend.fleet.infrastructure.persistence.jpa.repositories;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.Vehicle;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleId;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for Vehicle aggregate.
 *
 * @author Cobox Team
 * @version 1.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, VehicleId> {

    /**
     * Find vehicle by license plate
     */
    @Query("SELECT v FROM Vehicle v WHERE v.licensePlate.value = :licensePlate")
    Optional<Vehicle> findByLicensePlate(@Param("licensePlate") String licensePlate);

    /**
     * Find vehicles by status
     */
    @Query("SELECT v FROM Vehicle v WHERE v.status.value = :status")
    List<Vehicle> findByStatus(@Param("status") String status);

    /**
     * Find vehicles needing maintenance
     */
    @Query("SELECT v FROM Vehicle v WHERE v.status.value = 'MAINTENANCE' OR v.currentMileage > :mileageThreshold")
    List<Vehicle> findVehiclesNeedingMaintenance(@Param("mileageThreshold") BigDecimal mileageThreshold);

    /**
     * Find vehicles needing maintenance (without parameters)
     */
    @Query("SELECT v FROM Vehicle v WHERE v.status.value = 'MAINTENANCE' OR v.currentMileage > 100000")
    List<Vehicle> findVehiclesNeedingMaintenance();

    /**
     * Find operational vehicles
     */
    @Query("SELECT v FROM Vehicle v WHERE v.status.value = 'ACTIVE'")
    List<Vehicle> findOperationalVehicles();

    /**
     * Find vehicles by brand and model
     */
    @Query("SELECT v FROM Vehicle v WHERE v.brand = :brand AND v.model = :model")
    List<Vehicle> findByBrandAndModel(@Param("brand") String brand, @Param("model") String model);

    /**
     * Count vehicles by status
     */
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status.value = :status")
    Long countByStatus(@Param("status") String status);

    /**
     * Find vehicles with high mileage
     */
    @Query("SELECT v FROM Vehicle v WHERE v.currentMileage > :mileage ORDER BY v.currentMileage DESC")
    List<Vehicle> findVehiclesWithHighMileage(@Param("mileage") BigDecimal mileage);
}