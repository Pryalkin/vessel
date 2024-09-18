package com.bsuir.vessel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private Date sailingDate;
    private Date arrivalDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Port sailingPort;
    @ManyToOne(fetch = FetchType.LAZY)
    private Port arrivalPort;
    @ManyToOne(fetch = FetchType.LAZY)
    private Vessel vessel;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "flight_user",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> passengers = new ArrayList<>();

    public void addPassenger(User user) {
        passengers.add(user);
        user.getFlights().add(this);
    }

    public void removePassenger(User user) {
        passengers.remove(user);
        user.getFlights().remove(this);
    }

}
