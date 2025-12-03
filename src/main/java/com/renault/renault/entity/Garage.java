package com.renault.renault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "garages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garage", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<OpeningTime> openingTimes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "garage", orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    @Column(name = "vehicle_count", nullable = false)
    private Integer vehicleCount = 0;

    public static final int MAX_VEHICLES = 50;

    public boolean canAddVehicle() {
        return vehicleCount < MAX_VEHICLES;
    }

    public void addVehicle() {
        if (!canAddVehicle()) {
            throw new IllegalStateException("Le garage a atteint la limite de 50 véhicules");
        }
        vehicleCount++;
    }

    public void removeVehicle() {
        if (vehicleCount > 0) {
            vehicleCount--;
        }
    }
}
