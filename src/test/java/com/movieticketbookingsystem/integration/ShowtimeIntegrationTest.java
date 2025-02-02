package com.movieticketbookingsystem.integration;

import com.movieticketbookingsystem.entity.Movie;
import com.movieticketbookingsystem.entity.Showtime;
import com.movieticketbookingsystem.entity.Theater;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;  // Needed for "post()"
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class ShowtimeIntegrationTest {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Autowired
    private MockMvc mockMvc;

    private static String jwtToken;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() throws Exception {
        mysqlContainer.start();
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
        mysqlContainer.stop();
    }

    @BeforeEach
    public void registerUserBeforeTest() throws Exception {
        // Create the registration payload (adjust it to your DTO structure)
        String registerPayload = "{\"fullName\":\"testuser\",\"password\":\"password123\",\"email\":\"test4@example.com\",\"role\":\"ADMIN\"}";

        // Register the user by sending a POST request to the /register endpoint
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")  // Adjust to your registration endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isCreated())  // Check that the user is created successfully
                .andReturn();

        // After registration, authenticate the user to get the JWT token
        String loginResponse = JwtTestUtil.getJwtToken(mockMvc, "test4@example.com", "password123");

        jwtToken = extractTokenFromResponse(loginResponse);
    }

    // Method to extract token from the JSON response
    private String extractTokenFromResponse(String responseContent) throws Exception {
        JsonNode responseJson = objectMapper.readTree(responseContent);

        return responseJson.get("token").asText();
    }

    @Test
    public void testShowtimeHappyflow() throws Exception {
        // Create a movie
        Movie movie = new Movie("Inception", "Sci-Fi", 148, 8, 2010);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .content(objectMapper.writeValueAsString(movie)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // POST to create a showtime
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.rating").value(8))
                .andReturn();

        // Extract the response content as a String
        String responseContent = result.getResponse().getContentAsString();

        // Deserialize the response into a Movie object (you could also use any DTO or other object)
        Movie createdMovie = objectMapper.readValue(responseContent, Movie.class);


        Showtime showtime = new Showtime();
//        showtime.setMovie(createdMovie);
        showtime.setStartTime(LocalDateTime.parse("2025-02-11T10:00:00"));
        showtime.setEndTime(LocalDateTime.parse("2025-02-11T13:30:00"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/showtimes/2/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(showtime)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startTime").value("2025-02-11T10:00:00"));

        // Update a showtime
        Showtime updatedShowtime = new Showtime();
        updatedShowtime.setStartTime(LocalDateTime.parse("2025-02-11T14:00:00"));
        updatedShowtime.setEndTime(LocalDateTime.parse("2025-02-11T18:00:00"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/showtimes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(updatedShowtime)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("2025-02-11T14:00:00"));


        // Get showtime by movie
        MvcResult resultList = mockMvc.perform(MockMvcRequestBuilders.get("/api/showtimes/movie/{movieId}", createdMovie.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movie.id").value(createdMovie.getId()))
                .andReturn();


        // Delete showtime
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/showtimes/{id}", 1)  // Assuming the showtime with ID 1 exists
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }

}
