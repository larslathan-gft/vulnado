package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Value("${app.secret}")
    private String secret;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void comments_ShouldReturnListOfComments() throws Exception {
        List<Comment> comments = Arrays.asList(new Comment("user1", "comment1"), new Comment("user2", "comment2"));
        when(commentService.fetch_all()).thenReturn(comments);

        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                .header("x-auth-token", "valid_token")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].body").value("comment1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].body").value("comment2"));

        verify(commentService, times(1)).fetch_all();
    }

    @Test
    public void createComment_ShouldCreateAndReturnComment() throws Exception {
        CommentRequest request = new CommentRequest();
        request.username = "user1";
        request.body = "comment1";

        Comment comment = new Comment("user1", "comment1");
        when(commentService.create(request.username, request.body)).thenReturn(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments")
                .header("x-auth-token", "valid_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("comment1"));

        verify(commentService, times(1)).create(request.username, request.body);
    }

    @Test
    public void deleteComment_ShouldDeleteAndReturnTrue() throws Exception {
        when(commentService.delete("1")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/1")
                .header("x-auth-token", "valid_token")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        verify(commentService, times(1)).delete("1");
    }

    @Test
    public void deleteComment_ShouldReturnFalseWhenCommentNotFound() throws Exception {
        when(commentService.delete("1")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/comments/1")
                .header("x-auth-token", "valid_token")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));

        verify(commentService, times(1)).delete("1");
    }
}

// Existing Tests
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }
}
