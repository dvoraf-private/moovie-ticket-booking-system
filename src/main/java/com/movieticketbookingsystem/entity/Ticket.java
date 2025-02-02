package com.movieticketbookingsystem.entity;

import jakarta.persistence.*;

@Table(name="tickets")
@Entity
public class Ticket {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Showtime showtime;

    private int seatNumber;
    private double price;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

//    public void setMovie(Movie movie) {
//        this.movie = movie;
//    }
//
//    public Movie getMovie() {
//        return movie;
//    }

    public double getPrice() {
        return price;
    }

    public int getSeatNumber() {
        return seatNumber;
    }


    public Showtime getShowtime() {
        return showtime;
    }

    public User getUser() {
        return user;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setShowtime(Showtime showtime) {
        this.showtime = showtime;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
