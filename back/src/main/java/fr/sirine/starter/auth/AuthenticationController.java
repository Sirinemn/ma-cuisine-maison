package fr.sirine.starter.auth;


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
    public ResponseEntity<?> currentUserName(Authentication authentication)  {
        // Vérifier que l'utilisateur est authentifié
        if (authentication == null || !authentication.isAuthenticated()) {
            // Retourner 401 Unauthorized si l'utilisateur n'est pas authentifié
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non authentifié.");
        }

        // Récupérer le nom de l'utilisateur à partir de l'authentification
        String email = authentication.getName();
        User optionalUser = userService.findByEmail(email);

        // Si l'utilisateur n'est pas trouvé dans la base de données, retourner une erreur 404
        if (optionalUser== null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }

        // Renvoyer l'utilisateur trouvé
        return ResponseEntity.ok(optionalUser);
    }
}
