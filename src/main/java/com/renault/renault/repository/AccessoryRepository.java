package com.renault.renault.repository;

import com.renault.renault.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    List<Accessory> findByVehicle_Id(Long vehicleId);
}
