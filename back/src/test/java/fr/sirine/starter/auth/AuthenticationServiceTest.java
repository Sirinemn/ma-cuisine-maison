package fr.sirine.starter.auth;

import fr.sirine.starter.email.EmailService;
import fr.sirine.starter.email.EmailTemplateName;
import fr.sirine.starter.role.Role;
import fr.sirine.starter.role.RoleRepository;
import fr.sirine.starter.security.JwtService;
import fr.sirine.starter.user.Token;
import fr.sirine.starter.user.TokenRepository;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;


import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock

    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        Field field = AuthenticationService.class.getDeclaredField("activationUrl");
        field.setAccessible(true);
        field.set(authenticationService, "http://localhost:8080/activate");
    }



    @Test
    public void testRegisterSuccess() throws Exception {
        RegistrationRequest request = new RegistrationRequest("first", "last", "pseudo", LocalDateTime.of(2000, 1, 1, 10, 10, 0), "email@example.com", "password");
        Role userRole = new Role();
        userRole.setName("USER");
        User user = new User();

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // No exceptions expected
        authenticationService.register(request);

        // Vérifie que l'email de validation est envoyé avec les bons arguments
        verify(emailService, times(1)).sendEmail(
                eq("email@example.com"),
                eq("first last"),
                eq(EmailTemplateName.ACTIVATE_ACCOUNT),
                eq("http://localhost:8080/activate"), // L'URL d'activation doit être présente ici
                anyString(),
                eq("Account activation")
        );
    }

    @Test
    public void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest("email@example.com", "password");
        User user = new User();
        user.setId(1);
        user.setPseudo("pseudo");
        user.setFirstname(" name");
        user.setLastname(" last");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(any(Map.class), any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals(1, response.getUserId());
        assertEquals("pseudo", response.getPseudo());
        assertEquals("jwtToken", response.getToken());
    }


    @Test
    public void testActivateAccountSuccess() throws Exception {
        // Crée un utilisateur factice
        User user = new User();
        user.setId(1);
        user.setEmail("email@example.com");
        user.setLastname("Last");
        user.setFirstname("first");
        user.setEnabled(false);

        // Crée un token factice associé à l'utilisateur
        Token token = new Token();
        token.setToken("123456");
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        // Simuler les appels des repository
        when(tokenRepository.findByToken("123456")).thenReturn(Optional.of(token));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Exécuter la méthode à tester
        authenticationService.activateAccount("123456");

        // Vérifie que l'utilisateur a été activé
        assertTrue(user.isEnabled());

        // Vérifie que le token a été validé (l'heure de validation a été définie)
        verify(tokenRepository, times(1)).save(any(Token.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testActivateAccountTokenExpired() throws Exception {
        User user = new User();
        Token token = new Token();
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1)); // Expired token

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));

        // Exception expected
        Exception exception = assertThrows(Exception.class, () -> {
            authenticationService.activateAccount("expiredToken");
        });

        assertEquals("Code invalid an other code has been send to you email", exception.getMessage());
    }

    @Test
    public void testRegisterRoleNotFound() {
        RegistrationRequest request = new RegistrationRequest("first", "last", "pseudo", LocalDateTime.of(2000, 1, 1, 10, 10, 10), "email@example.com", "password");

        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        // Exception expected
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            authenticationService.register(request);
        });

        assertEquals("ROLE USER was not initiated", exception.getMessage());
    }
}