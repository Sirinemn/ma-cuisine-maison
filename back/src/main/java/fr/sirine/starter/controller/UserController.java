package fr.sirine.starter.controller;

import fr.sirine.cuisine.payload.MessageResponse;
import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@Tag(name="User", description = "API de gestion des utilisateurs")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper)
    {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @Operation(summary = "Récupérer un utilisateur par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) throws IOException {

        User user = userService.findById(Integer.parseInt(id));
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@RequestParam("pseudo") @NotBlank @Size(max = 63) String pseudo, @RequestParam("firstname") @NotBlank @Size(max = 63) String firstname, @RequestParam("lastname") @NotBlank @Size(max = 63) String lastname, @PathVariable Integer id) {
        userService.updateUser(pseudo, firstname, lastname, id);
        MessageResponse messageResponse = new MessageResponse("Updated with success!");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
