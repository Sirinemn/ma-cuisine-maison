package fr.sirine.starter.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sirine.starter.auth.AuthenticationRequest;
import fr.sirine.starter.auth.RegistrationRequest;
import fr.sirine.starter.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Nettoyer la base de données
        tokenRepository.deleteAll();
        userRepository.deleteAll();

    }
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Test
    void shouldRegisterSuccessfully() throws Exception {

        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .dateOfBirth(LocalDateTime.now())
                .email("john@mail.com")
                .firstname("John")
                .lastname("Doe")
                .password("password")
                .pseudo("pseudo")
                .build();
        String content = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isAccepted());
    }

    @Test
    void  shouldActivateAccount() throws Exception {

        User user = new User();
        user.setFirstname("First");
        user.setLastname("Last");
        user.setEmail("first.last@mail.com");
        user.setPseudo("FirstL");
        user.setPassword(passwordEncoder().encode("testpassword"));
        user.setEnabled(false);
        userRepository.save(user);
        Token token = new Token();
        token.setToken("test-token");
        token.setUser(user);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        token.setValidatedAt(LocalDateTime.now().plusMinutes(5));
        tokenRepository.save(token);

        mockMvc.perform(get("/auth/activate-account")
                        .param("token", "test-token"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldAuthenticateSuccessfuly() throws Exception {

        User user = new User();
        user.setId(2);
        user.setFirstname("Ana");
        user.setLastname("Pure");
        user.setEmail("ana@mail.com");
        user.setPseudo("AnaP");
        user.setPassword(passwordEncoder().encode("testpassword"));
        user.setEnabled(true);
        userRepository.save(user);

        AuthenticationRequest authenticationRequest =  AuthenticationRequest.builder()
                .email("ana@mail.com")
                .password("testpassword")
                .build();
        String content = objectMapper.writeValueAsString(authenticationRequest);
        mockMvc.perform(post("/auth/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andExpect(status().isOk());
    }

    @Test
    void shouldFailAuthenticateDisabledAccount() throws Exception {

        User user = new User();
        user.setId(10);
        user.setFirstname("Ana");
        user.setLastname("Pure");
        user.setEmail("ana@mail.com");
        user.setPseudo("AnaP");
        user.setPassword(passwordEncoder().encode("testpassword"));
        user.setEnabled(false);
        userRepository.save(user);

        AuthenticationRequest authenticationRequest =  AuthenticationRequest.builder()
                .email("ana@mail.com")
                .password("testpassword")
                .build();
        String content = objectMapper.writeValueAsString(authenticationRequest);
        mockMvc.perform(post("/auth/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCurrentUserName_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Utilisateur non authentifié."));
    }

    @Test
    @WithMockUser(username = "nonexistent@example.com")
    public void testCurrentUserName_userNotFound() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Utilisateur non trouvé."));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    public void testCurrentUserName_authenticatedUser() throws Exception {
        User user = new User();
        user.setId(5);
        user.setFirstname("test");
        user.setLastname("test");
        user.setEmail("test@example.com");
        user.setPseudo("test");
        user.setPassword(passwordEncoder().encode("testpassword"));
        user.setEnabled(false);
        userRepository.save(user);

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
