package com.renault.renault.repository;

import com.renault.renault.entity.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    Page<Garage> findAll(Pageable pageable);
    List<Garage> findByNameContainingIgnoreCase(String name);
    List<Garage> findByVehicles_Model(String model);
    List<Garage> findByVehicles_FuelType(String fuelType);
    List<Garage> findByVehicles_Accessories_Name(String accessoryName);
}
