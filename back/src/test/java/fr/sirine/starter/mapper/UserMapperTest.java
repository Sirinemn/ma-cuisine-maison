package fr.sirine.starter.mapper;

import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.user.User;
import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


import fr.sirine.starter.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @InjectMocks
    private UserMapperImpl userMapper; // Utiliser l'implémentation générée par MapStruct

    private User user;
    private UserDto userDto;
    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setName("USER");

        user = User.builder()
                .id(1)
                .email("john@mail.fr")
                .pseudo("JohnD")
                .firstname("John")
                .lastname("Doe")
                .roles(List.of(role))
                .build();

        userDto = UserDto.builder()
                .id(1)
                .email("john@mail.fr")
                .pseudo("JohnD")
                .firstname("John")
                .lastname("Doe")
                .roles(List.of("USER"))
                .build();
    }

    @Test
    public void testToDto() throws IOException {
        UserDto result = userMapper.toDto(user);

        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPseudo(), result.getPseudo());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()), result.getRoles());
    }

    @Test
    public void testToEntity() throws IOException {
        User result = userMapper.toEntity(userDto);

        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getPseudo(), result.getPseudo());
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getRoles().stream().map(String::valueOf).collect(Collectors.toList()), result.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
    }

}