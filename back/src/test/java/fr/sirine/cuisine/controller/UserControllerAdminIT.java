package fr.sirine.cuisine.controller;

import fr.sirine.MaCuisineMaison;
import fr.sirine.starter.dto.UserDto;
import fr.sirine.starter.mapper.UserMapper;
import fr.sirine.starter.user.User;
import fr.sirine.starter.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MaCuisineMaison.class)
@AutoConfigureMockMvc
public class UserControllerAdminIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .email("test@example.com")
                .pseudo("testuser")
                .firstname("John")
                .lastname("Doe")
                .build();

        userDto = UserDto.builder()
                .id(1)
                .email("test@example.com")
                .pseudo("testuser")
                .firstname("John")
                .lastname("Doe")
                .build();
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    public void shouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].pseudo").value("testuser"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    public void shouldReturnUserById() throws Exception {
        when(userService.findById(1)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.pseudo").value("testuser"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void should_return_accessDenied() throws Exception {
        // Arrange
        int userId = 1;
        // Act & Assert
        mockMvc.perform(get("/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.error").value("Access Denied"));


    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    public void shouldUpdateUser() throws Exception {
        String newPseudo = "newuser";
        String newFirstname = "New";
        String newLastname = "User";

        mockMvc.perform(put("/admin/users/1")
                        .param("pseudo", newPseudo)
                        .param("firstname", newFirstname)
                        .param("lastname", newLastname))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated with success!"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    public void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/admin/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1);
    }
}
