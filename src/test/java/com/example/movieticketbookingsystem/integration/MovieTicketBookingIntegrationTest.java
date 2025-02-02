package com.example.movieticketbookingsystem.integration;

import com.example.movieticketbookingsystem.model.Movie;
import com.example.movieticketbookingsystem.service.MovieService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.Testcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;  // Needed for "post()"
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class MovieTicketBookingIntegrationTest {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() {
        // Set Spring Boot application properties dynamically for MySQL
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
        System.setProperty("spring.datasource.platform", "mysql");
    }

    @AfterAll
    public static void tearDown() {
        System.clearProperty("spring.datasource.url");
        System.clearProperty("spring.datasource.username");
        System.clearProperty("spring.datasource.password");
        System.clearProperty("spring.datasource.driver-class-name");
        System.clearProperty("spring.datasource.platform");
    }

    @Test
    public void testCreateMovie() throws Exception {
        // Example test for creating a movie, similar to the previous one
        Movie movie = new Movie("Inception", "Sci-Fi", 148, 8, 2010);


        // POST to create a movie
        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.rating").value(8));
    }

//    @Test
//    public void testGetMovie() throws Exception {
//        Movie movie = movieService.createMovie(new Movie("Inception", "Sci-Fi", 148, 8.8, 2010));
//
//        // GET movie by ID
//        mockMvc.perform(get("/api/movies/{id}", movie.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Inception"));
//    }

}
