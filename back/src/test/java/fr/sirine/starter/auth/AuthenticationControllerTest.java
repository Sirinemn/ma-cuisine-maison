package fr.sirine.starter.auth;

import fr.sirine.starter.MonStarter;

import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MonStarter.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    UserService userService;

    @MockBean
    Authentication authentication;

    @Test
    void shouldRegisterSuccessfully() throws Exception {

        // Create a mock request payload
        String registrationRequestJson = "{ \"firstname\": \"John\", \"lastname\": \"Doe\", \"pseudo\": \"john_doe\",  \"dateOfBirth\":\"2000-01-01T10:10:00\", \"email\": \"test@example.com\" , \"password\": \"password\" }";

        // Call the register method via MockMvc
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationRequestJson))
                .andExpect(status().isAccepted());

        // Verify if the service method was called
        verify(authenticationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    void shouldActivateAccount() throws Exception {
        // Simuler la méthode activateAccount dans AuthenticationService
        Mockito.doNothing().when(authenticationService).activateAccount("test-token");

        mockMvc.perform(get("/auth/activate-account")
                        .param("token", "test-token"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAuthenticateSuccessfully() throws Exception {
        String authenticationRequestJson = "{ \"email\": \"test@example.com\", \"password\": \"password\" }";

        // Simuler la méthode authenticate dans AuthenticationService
        when(authenticationService.authenticate(any())).thenReturn(new AuthenticationResponse("dummy-jwt-token","pseudo", 1));

        mockMvc.perform(post("/auth/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"));
    }

    // Test pour obtenir les informations de l'utilisateur courant
    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void shouldReturnCurrentUser() throws Exception {
        // Simuler la méthode findByEmail dans UserService
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");

        when(userService.findByEmail("test@example.com")).thenReturn(user);

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }

}