package com.movieticketbookingsystem.repository;

import com.movieticketbookingsystem.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Theater findByName(String name);
}
