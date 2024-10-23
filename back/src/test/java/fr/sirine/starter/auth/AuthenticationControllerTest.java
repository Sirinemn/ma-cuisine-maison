package fr.sirine.starter.auth;

import fr.sirine.MaCuisineMaison;

import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
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


import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MaCuisineMaison.class)
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
    UserMapper userMapper;

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

        // Build the expected AuthenticationResponse
        AuthenticationResponse authResponse = AuthenticationResponse.builder()
                .token("dummy-jwt-token")
                .pseudo("pseudo")
                .userId(1)
                .roles(List.of("ROLE_USER")) // Assuming roles are returned like this
                .build();

        // Mock the authenticate method
        when(authenticationService.authenticate(any())).thenReturn(authResponse);

        // Perform the request and check the response
        mockMvc.perform(post("/auth/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"))
                .andExpect(jsonPath("$.pseudo").value("pseudo"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    // Test pour obtenir les informations de l'utilisateur courant
    @Test
    @WithMockUser(username = "test@example.com", roles = {"USER"})
    void shouldReturnCurrentUser() throws Exception {
        // Mocking user service response
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstname("John");
        user.setLastname("Doe");
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        // Mocking user mapper response
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"));
    }


}