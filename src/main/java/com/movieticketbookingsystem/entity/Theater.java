package com.movieticketbookingsystem.entity;

import jakarta.persistence.*;

//import javax.persistence.*;


@Table(name="theaters")
@Entity
public class Theater {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location; // You might want to store the location of the theater (city, address, etc.)

//    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Showtime> showtimes;

    // Constructors, Getters, Setters
    public Theater() {
    }

    public Theater(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public List<Showtime> getShowtimes() {
//        return showtimes;
//    }
//
//    public void setShowtimes(List<Showtime> showtimes) {
//        this.showtimes = showtimes;
//    }

}
