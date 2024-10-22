package fr.sirine.cuisine.controller;

import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserControllerAdmin {

    private final UserService userService;

    public UserControllerAdmin(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    @Operation(summary = "Récupérer un utilisateur par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) throws IOException {

        User user = userService.findById(Integer.parseInt(id));
        return ResponseEntity.ok(user);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@RequestParam("pseudo") @NotBlank @Size(max = 63) String pseudo, @RequestParam("firstname") @NotBlank @Size(max = 63) String firstname, @RequestParam("lastname") @NotBlank @Size(max = 63) String lastname, @PathVariable Integer id) {
        userService.updateUser( pseudo, firstname, lastname, id);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
