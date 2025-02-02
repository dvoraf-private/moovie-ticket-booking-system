package com.movieticketbookingsystem.integration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JwtTestUtil {

    public static String getJwtToken(MockMvc mockMvc, String email, String password) throws Exception {
        // Create the login payload
        String loginPayload = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

        // Send login request and capture the JWT token
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")  // Adjust to your login endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JWT token from the response
        return result.getResponse().getContentAsString();
    }
}
