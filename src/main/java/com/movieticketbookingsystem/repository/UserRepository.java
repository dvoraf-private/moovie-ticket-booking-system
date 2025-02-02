package com.movieticketbookingsystem.repository;

import com.movieticketbookingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
//    User findByName(String username);

}
