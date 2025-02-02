//package com.movieticketbookingsystem.integration;
//
//import com.movieticketbookingsystem.entity.Movie;
//import com.movieticketbookingsystem.entity.Showtime;
//import com.movieticketbookingsystem.entity.Ticket;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.JsonNode;
//
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Testcontainers
//@SpringBootTest
//@AutoConfigureMockMvc
//public class CustomerUserIntegrationTest {
//
//    @Container
//    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass");
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private static String jwtToken;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeAll
//    public static void setUp() throws Exception {
//        mysqlContainer.start();
//        // Set Spring Boot application properties dynamically for MySQL
//        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
//        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
//        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
//        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
//        System.setProperty("spring.datasource.platform", "mysql");
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        System.clearProperty("spring.datasource.url");
//        System.clearProperty("spring.datasource.username");
//        System.clearProperty("spring.datasource.password");
//        System.clearProperty("spring.datasource.driver-class-name");
//        System.clearProperty("spring.datasource.platform");
//        mysqlContainer.stop();
//    }
//
//    @BeforeEach
//    public void registerUserBeforeTest() throws Exception {
//        System.out.println("call 1");
//        // Create the registration payload (adjust it to your DTO structure)
//        String registerPayload = "{\"fullName\":\"testuser\",\"password\":\"password123\",\"email\":\"test4@example.com\",\"role\":\"ADMIN\"}";
//
//        // Register the user by sending a POST request to the /register endpoint
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")  // Adjust to your registration endpoint
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(registerPayload))
//                .andExpect(status().isCreated())
//                .andReturn();
//
//        // After registration, authenticate the user to get the JWT token
//        String loginResponse = JwtTestUtil.getJwtToken(mockMvc, "test4@example.com", "password123");
//
//        jwtToken = extractTokenFromResponse(loginResponse);
//    }
//
//    // Method to extract token from the JSON response
//    private String extractTokenFromResponse(String responseContent) throws Exception {
//        JsonNode responseJson = objectMapper.readTree(responseContent);
//
//        return responseJson.get("token").asText();  // Extract and return the token field
//    }
//
//    @Test
//    public void testCustomerCannotAddMovie() throws Exception {
//        Movie movie = new Movie("Inception", "Sci-Fi", 148, 8, 2010);
//
//        String customerToken = JwtTestUtil.getJwtToken(mockMvc, "customeruser", "customerpass");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/movies")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken)
//                        .content(objectMapper.writeValueAsString(movie)))
//                .andExpect(status().isForbidden());  // Forbidden for non-admin users
//
//        // Test customer cannot delete moovie
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movies/{id}", 1)  // Assuming movie with ID 1 exists
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken))
//                .andExpect(status().isForbidden());
//
//        // Test customer cannot add showtime
//        Showtime showtime = new Showtime(1, "Theater1", "2025-02-01T12:00:00", "2025-02-01T14:00:00");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/showtimes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken)
//                        .content(objectMapper.writeValueAsString(showtime)))
//                .andExpect(status().isForbidden());
//
//        // test Customer book tickets
//        Ticket booking = new Ticket(1, 1, "A1", 15.0);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/tickets/book")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + customerToken)
//                        .content(objectMapper.writeValueAsString(booking)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.seatNumber").value("A1"))
//                .andExpect(jsonPath("$.price").value(15.0));
//    }
//
//
//
//
//}
