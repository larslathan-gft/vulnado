import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private LinkLister linkLister;

    private MockMvc mockMvc;

    private void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void links_ShouldReturnLinks() throws Exception {
        setup();
        String url = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");

        // Mocking the LinkLister.getLinks method
        Mockito.when(linkLister.getLinks(url)).thenReturn(expectedLinks);

        mockMvc.perform(get("/links")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"http://example.com/link1\", \"http://example.com/link2\"]"));
    }

    @Test
    public void links_ShouldHandleIOException() throws Exception {
        setup();
        String url = "http://example.com";

        // Mocking the LinkLister.getLinks method to throw IOException
        Mockito.when(linkLister.getLinks(url)).thenThrow(new IOException("IO Exception"));

        mockMvc.perform(get("/links")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        setup();
        String url = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");

        // Mocking the LinkLister.getLinksV2 method
        Mockito.when(linkLister.getLinksV2(url)).thenReturn(expectedLinks);

        mockMvc.perform(get("/links-v2")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"http://example.com/link1\", \"http://example.com/link2\"]"));
    }

    @Test
    public void linksV2_ShouldHandleBadRequest() throws Exception {
        setup();
        String url = "http://example.com";

        // Mocking the LinkLister.getLinksV2 method to throw BadRequest
        Mockito.when(linkLister.getLinksV2(url)).thenThrow(new BadRequest("Bad Request"));

        mockMvc.perform(get("/links-v2")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
