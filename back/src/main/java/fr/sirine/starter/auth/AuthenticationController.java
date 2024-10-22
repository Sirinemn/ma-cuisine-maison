package fr.sirine.starter.auth;


import fr.sirine.starter.mapper.UserMapper;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name="Authentication")
public class AuthenticationController {

    public final AuthenticationService authenticationService;
    public final UserService userService;
    private final UserMapper userMapper;
    @Operation(summary = "Inscrire un utilisateur")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException, Exception {
        authenticationService.register(request);
        return ResponseEntity.accepted().build();
    }
    @Operation(summary = "Connecter un utilisateur")
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody @Valid AuthenticationRequest request
    ){
        return  ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @Operation(summary = "Activer le compte de l'utilisateur par un token")
    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws Exception {
        authenticationService.activateAccount(token);
    }
    @Operation(summary = "Récupérer l'utilisateur connecté à l'application")
    @GetMapping("/me")
    public ResponseEntity<?> currentUserName(Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
        }
        String email = authentication.getName();
        User optionalUser = userService.findByEmail(email);
        if (optionalUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }
        if (optionalUser.getFirstname() == null || optionalUser.getLastname() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prénom et nom de famille sont requis.");
        }
        return ResponseEntity.ok(userMapper.toDto(optionalUser));
    }
}
