package fr.sirine.starter.controller;

import fr.sirine.starter.MonStarter;
import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;



import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MonStarter.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnUserDto_whenGetUserIsCalled() throws Exception {
        // Arrange
        int userId = 1;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setPseudo("pseudo");
        mockUser.setEmail("john@mail.fr");

        UserDto mockUserDto =  UserDto.builder()
                .email("john@mail.fr")
                .id(userId)
                .pseudo("pseudo")
                .build();


        when(userService.findById(userId)).thenReturn(mockUser);
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        // Act & Assert
        mockMvc.perform(get("/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.pseudo").value("pseudo"))
                .andExpect(jsonPath("$.email").value("john@mail.fr"));

        // Verify the interactions with the service and mapper
        verify(userService, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(mockUser);
    }

}