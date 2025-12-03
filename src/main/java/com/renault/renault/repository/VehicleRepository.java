package com.renault.renault.repository;

import com.renault.renault.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByGarage_Id(Long garageId);
    List<Vehicle> findByModel(String model);
}
