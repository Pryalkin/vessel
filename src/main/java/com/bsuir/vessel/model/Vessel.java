package com.bsuir.vessel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Vessel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Integer numberOfPass;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    private Port port;
    @OneToMany(
            mappedBy = "vessel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.setVessel(this);
    }
}
