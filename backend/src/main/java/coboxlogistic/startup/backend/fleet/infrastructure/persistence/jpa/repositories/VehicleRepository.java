package coboxlogistic.startup.backend.fleet.infrastructure.persistence.jpa.repositories;

import coboxlogistic.startup.backend.fleet.domain.model.aggregates.Vehicle;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.LicensePlate;
import coboxlogistic.startup.backend.fleet.domain.model.valueobjects.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for Vehicle aggregate in the Fleet bounded context.
 * 
 * @author Cobox Team
 * @version 1.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    /**
     * Find vehicle by license plate
     * @param licensePlate the license plate
     * @return the vehicle if found
     */
    Optional<Vehicle> findByLicensePlate(LicensePlate licensePlate);

    /**
     * Check if vehicle exists by license plate
     * @param licensePlate the license plate
     * @return true if vehicle exists
     */
    boolean existsByLicensePlate(LicensePlate licensePlate);

    /**
     * Find vehicles by status
     * @param status the vehicle status
     * @return list of vehicles with the specified status
     */
    List<Vehicle> findByStatus(VehicleStatus status);

    /**
     * Find operational vehicles (ACTIVE status)
     * @return list of operational vehicles
     */
    @Query("SELECT v FROM Vehicle v WHERE v.status = :status")
    List<Vehicle> findOperationalVehicles(@Param("status") VehicleStatus status);

    /**
     * Find vehicles that need maintenance
     * @return list of vehicles that need maintenance
     */
    @Query("SELECT v FROM Vehicle v WHERE v.currentMileage > 100000 OR v.status = 'MAINTENANCE'")
    List<Vehicle> findVehiclesNeedingMaintenance();

    /**
     * Find vehicles by brand
     * @param brand the brand
     * @return list of vehicles with the specified brand
     */
    List<Vehicle> findByBrand(String brand);

    /**
     * Find vehicles by fuel type
     * @param fuelType the fuel type
     * @return list of vehicles with the specified fuel type
     */
    List<Vehicle> findByFuelType(coboxlogistic.startup.backend.fleet.domain.model.valueobjects.FuelType fuelType);

    /**
     * Count vehicles by status
     * @param status the vehicle status
     * @return count of vehicles with the specified status
     */
    long countByStatus(VehicleStatus status);
} 