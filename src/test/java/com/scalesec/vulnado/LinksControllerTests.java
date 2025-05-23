import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private LinkLister linkLister;

    @Test
    public void links_ShouldReturnLinks() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String url = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");

        Mockito.when(linkLister.getLinks(url)).thenReturn(expectedLinks);

        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"http://example.com/link1\",\"http://example.com/link2\"]"));
    }

    @Test
    public void links_ShouldHandleIOException() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String url = "http://example.com";

        Mockito.when(linkLister.getLinks(url)).thenThrow(new IOException("IO Exception"));

        mockMvc.perform(MockMvcRequestBuilders.get("/links")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String url = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");

        Mockito.when(linkLister.getLinksV2(url)).thenReturn(expectedLinks);

        mockMvc.perform(MockMvcRequestBuilders.get("/links-v2")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"http://example.com/link1\",\"http://example.com/link2\"]"));
    }

    @Test
    public void linksV2_ShouldHandleBadRequest() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String url = "http://example.com";

        Mockito.when(linkLister.getLinksV2(url)).thenThrow(new BadRequest("Bad Request"));

        mockMvc.perform(MockMvcRequestBuilders.get("/links-v2")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
