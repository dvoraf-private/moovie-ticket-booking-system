//package com.movieticketbookingsystem.integration;
//
//import com.movieticketbookingsystem.entity.Movie;
//import com.movieticketbookingsystem.entity.Showtime;
//import com.movieticketbookingsystem.entity.Ticket;
//import com.movieticketbookingsystem.entity.User;
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
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@Testcontainers
//@SpringBootTest
//@AutoConfigureMockMvc
//public class TicketIntegrationTest {
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
//    public void testTicketHappyflow() throws Exception {
//
//        // Book a ticket
////        Ticket booking = new Ticket(1, 1, "A1", 15.0);  // Example for movie ID 1, showtime ID 1, seat A1
//        Ticket ticket = new Ticket();
//        ticket.setUser(new User());
//        ticket.setShowtime(new Showtime());
//        ticket.setSeatNumber(1);
//        ticket.setPrice(50.0);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/tickets/book/")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
//                        .content(objectMapper.writeValueAsString(ticket)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.seatNumber").value("A1"))
//                .andExpect(jsonPath("$.price").value(15.0));
//
//        // Seat already booked
//        Ticket newTicket = new Ticket();
//        newTicket.setSeatNumber(1);
//        newTicket.setPrice(15.0);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/bookings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
//                        .content(objectMapper.writeValueAsString(newTicket)))
//                .andExpect(status().isConflict());
//
//        // Get all tickets by id
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/bookings/{id}", 1)  // Assuming booking with ID 1 exists
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.seatNumber").value("A1"))
//                .andExpect(jsonPath("$.price").value(15.0));
//    }
//
//
//
//
//}
