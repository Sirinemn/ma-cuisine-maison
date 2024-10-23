package fr.sirine.starter.user;

import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
import fr.sirine.starter.role.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    LocalDateTime rightNow = LocalDateTime.now();
    User initialUser = User.builder()
            .id(1)
            .email("john@mail.fr")
            .enabled(true)
            .accountLocked(true)
            .firstname("John")
            .lastname("Doe")
            .pseudo("JohnD")
            .createdDate(rightNow)
            .build();
    UserDto initialUserDto = UserDto.builder()
            .id(1)
            .email("john@mail.fr")
            .roles(Arrays.asList("USER"))
            .pseudo("JohnD")
            .lastname("Doe")
            .firstname("John")
            .dateOfBirth(rightNow)
            .build();

    @Test
    public void should_return_all_user() throws IOException {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        Role userRole = new Role();
        userRole.setName("USER");
        this.initialUser.setRoles(Arrays.asList(userRole));

        User adminUser = User.builder()
                .id(2)
                .email("admin@example.com")
                .pseudo("admin")
                .roles(Arrays.asList(adminRole))
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(initialUser, adminUser));
        when(userMapper.toDto(initialUser)).thenReturn(initialUserDto);

        List<UserDto> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("john@mail.fr", result.get(0).getEmail());
    }
    @Test
    public void test_update_user() {
        when(userRepository.findById(1)).thenReturn(Optional.of(initialUser));
        userService.updateUser("newPseudo", "newFirst", "newLast", 1);

        assertEquals("newPseudo", initialUser.getPseudo());
        assertEquals("newFirst", initialUser.getFirstname());
        assertEquals("newLast", initialUser.getLastname());
        verify(userRepository, times(1)).save(initialUser);
    }
    @Test
    public void should_find_user_by_id(){
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(initialUser));
        userService.findById(1);
        verify(userRepository, times(1)).findById(1);
        
    }
    @Test
    public void should_find_user_by_email(){
        when(userRepository.findByEmail("john@mail.fr")).thenReturn(Optional.ofNullable(initialUser));
        userService.findByEmail("john@mail.fr");
        verify(userRepository, times(1)).findByEmail("john@mail.fr");

    }
    @Test
    public void should_find_user_by_pseudo(){
        when(userRepository.findByPseudo("JohnD")).thenReturn(Optional.ofNullable(initialUser));
        userService.getUserByPseudo("JohnD");
        verify(userRepository, times(1)).findByPseudo("JohnD");

    }

}