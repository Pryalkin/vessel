package com.bsuir.vessel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Vessel> vessels = new ArrayList<>();

    public void addVessel(Vessel vessel) {
        vessels.add(vessel);
        vessel.setCompany(this);
    }

}
