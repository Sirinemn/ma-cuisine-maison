package fr.sirine.cuisine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sirine.MaCuisineMaison;
import fr.sirine.cuisine.comment.Comment;
import fr.sirine.cuisine.comment.CommentService;
import fr.sirine.cuisine.recipe.Recipe;
import fr.sirine.starter.role.Role;
import fr.sirine.starter.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = MaCuisineMaison.class)
public class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;
    private User user;
    private Recipe recipe;
    private Comment comment;

    @BeforeEach
    public void setup() {
        Role role = Role.builder()
                .name("USER")
                .build();
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user = User.builder()
                .id(1)
                .roles(roles)
                .pseudo("pseudo")
                .build();
        recipe = Recipe.builder()
                .id(1)
                .build();
        comment = Comment.builder()
                .content("dummy comment")
                .id(1)
                .recipe(recipe)
                .user(user)
                .build();
    }
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void createCommentTest() throws Exception {
        when(commentService.saveComment(any(Comment.class))).thenReturn(comment);
        String content = objectMapper.writeValueAsString(comment);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/comments")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(content);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Comment added with success!"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void getCommentsByRecipeTest() throws Exception {
        when(commentService.findByRecipeId(1)).thenReturn(List.of(comment));

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void deleteCommentTest() throws Exception {
        doNothing().when(commentService).deleteComment(1);

        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void updateCommentTest() throws Exception {
        when(commentService.updateComment(1, "new content")).thenReturn(comment);
        mockMvc.perform(put("/comments/1").param("content","new content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Updated with success!"))
                .andExpect(status().isOk());
    }
}
