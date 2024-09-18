package com.bsuir.vessel.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String role;
    private String[] authorities;
    @ManyToMany(mappedBy = "passengers")
    private List<Flight> flights = new ArrayList<>();

}