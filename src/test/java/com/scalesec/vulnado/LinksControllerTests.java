package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void links_ShouldReturnLinks() throws Exception {
        // Mocking LinkLister.getLinks method
        LinkLister linkListerMock = mock(LinkLister.class);
        when(linkListerMock.getLinks(anyString())).thenReturn(Arrays.asList("link1", "link2"));

        mockMvc.perform(get("/links")
                .param("url", "http://example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("link1"))
                .andExpect(jsonPath("$[1]").value("link2"));
    }

    @Test
    public void links_ShouldHandleIOException() throws Exception {
        // Mocking LinkLister.getLinks method to throw IOException
        LinkLister linkListerMock = mock(LinkLister.class);
        when(linkListerMock.getLinks(anyString())).thenThrow(new IOException("IO Exception"));

        try {
            mockMvc.perform(get("/links")
                    .param("url", "http://example.com")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        } catch (NestedServletException e) {
            // Expected exception
        }
    }

    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        // Mocking LinkLister.getLinksV2 method
        LinkLister linkListerMock = mock(LinkLister.class);
        when(linkListerMock.getLinksV2(anyString())).thenReturn(Arrays.asList("link1", "link2"));

        mockMvc.perform(get("/links-v2")
                .param("url", "http://example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("link1"))
                .andExpect(jsonPath("$[1]").value("link2"));
    }

    @Test
    public void linksV2_ShouldHandleBadRequest() throws Exception {
        // Mocking LinkLister.getLinksV2 method to throw BadRequest
        LinkLister linkListerMock = mock(LinkLister.class);
        when(linkListerMock.getLinksV2(anyString())).thenThrow(new BadRequest("Bad Request"));

        try {
            mockMvc.perform(get("/links-v2")
                    .param("url", "http://example.com")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (NestedServletException e) {
            // Expected exception
        }
    }
}
