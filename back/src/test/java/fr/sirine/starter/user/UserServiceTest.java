package fr.sirine.starter.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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