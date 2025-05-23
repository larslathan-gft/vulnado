package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

    @MockBean
    private LinkLister linkLister;

    private MockMvc mockMvc;

    private void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void links_ShouldReturnLinks() throws Exception {
        setupMockMvc();
        List<String> mockLinks = Arrays.asList("http://example.com", "http://example.org");
        when(linkLister.getLinks(anyString())).thenReturn(mockLinks);

        mockMvc.perform(get("/links")
                .param("url", "http://test.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("http://example.com"))
                .andExpect(jsonPath("$[1]").value("http://example.org"));

        verify(linkLister, times(1)).getLinks("http://test.com");
    }

    @Test
    public void links_ShouldHandleIOException() throws Exception {
        setupMockMvc();
        when(linkLister.getLinks(anyString())).thenThrow(new IOException("Test IOException"));

        try {
            mockMvc.perform(get("/links")
                    .param("url", "http://test.com")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        } catch (NestedServletException e) {
            // Expected exception
        }

        verify(linkLister, times(1)).getLinks("http://test.com");
    }

    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        setupMockMvc();
        List<String> mockLinks = Arrays.asList("http://example.com", "http://example.org");
        when(linkLister.getLinksV2(anyString())).thenReturn(mockLinks);

        mockMvc.perform(get("/links-v2")
                .param("url", "http://test.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("http://example.com"))
                .andExpect(jsonPath("$[1]").value("http://example.org"));

        verify(linkLister, times(1)).getLinksV2("http://test.com");
    }

    @Test
    public void linksV2_ShouldHandleBadRequest() throws Exception {
        setupMockMvc();
        when(linkLister.getLinksV2(anyString())).thenThrow(new BadRequest("Test BadRequest"));

        mockMvc.perform(get("/links-v2")
                .param("url", "http://test.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(linkLister, times(1)).getLinksV2("http://test.com");
    }
}
