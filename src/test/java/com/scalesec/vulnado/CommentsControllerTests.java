package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Comment commentMock;

    @Value("${app.secret}")
    private String secret;

    private static final String AUTH_TOKEN = "valid-auth-token";

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @Test
    public void comments_Get_ShouldReturnComments() throws Exception {
        // Mocking the fetch_all method
        List<Comment> mockComments = Arrays.asList(new Comment("user1", "body1"), new Comment("user2", "body2"));
        Mockito.when(Comment.fetch_all()).thenReturn(mockComments);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                .header("x-auth-token", AUTH_TOKEN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].body").value("body1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].body").value("body2"));
    }

    @Test
    public void createComment_Post_ShouldCreateComment() throws Exception {
        // Mocking the create method
        Comment mockComment = new Comment("user1", "body1");
        Mockito.when(Comment.create("user1", "body1")).thenReturn(mockComment);

        CommentRequest request = new CommentRequest();
        request.username = "user1";
        request.body = "body1";

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .header("x-auth-token", AUTH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("body1"));
    }

    @Test
    public void deleteComment_Delete_ShouldDeleteComment() throws Exception {
        // Mocking the delete method
        Mockito.when(Comment.delete("123")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/123")
                .header("x-auth-token", AUTH_TOKEN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    public void comments_Get_ShouldReturnUnauthorizedForInvalidToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                .header("x-auth-token", "invalid-token")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void createComment_Post_ShouldReturnBadRequestForInvalidInput() throws Exception {
        CommentRequest request = new CommentRequest();
        request.username = ""; // Invalid username
        request.body = ""; // Invalid body

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .header("x-auth-token", AUTH_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteComment_Delete_ShouldReturnNotFoundForInvalidId() throws Exception {
        // Mocking the delete method for invalid ID
        Mockito.when(Comment.delete("invalid-id")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/invalid-id")
                .header("x-auth-token", AUTH_TOKEN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

