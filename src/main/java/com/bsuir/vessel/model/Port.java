package com.bsuir.vessel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String water;
    private String width;
    private String length;
    private Integer count;
    @OneToMany(
            mappedBy = "port",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Vessel> vessels = new ArrayList<Vessel>();
    @OneToMany(
            mappedBy = "sailingPort",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Flight> sailingFlights = new ArrayList<>();
    @OneToMany(
            mappedBy = "arrivalPort",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Flight> arrivalFlights = new ArrayList<>();

    public void addVessel(Vessel vessel) {
        vessels.add(vessel);
        vessel.setPort(this);
    }

    public void addSailingFlight(Flight flight) {
        sailingFlights.add(flight);
        flight.setSailingPort(this);
    }

    public void addArrivalFlight(Flight flight) {
        arrivalFlights.add(flight);
        flight.setArrivalPort(this);
    }

}
