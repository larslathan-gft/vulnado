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

    @Test
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void links_ShouldReturnLinks() throws Exception {
        String url = "http://example.com";
        List<String> links = Arrays.asList("http://example.com/link1", "http://example.com/link2");

        Mockito.when(linkLister.getLinks(url)).thenReturn(links);

        mockMvc.perform(get("/links")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"http://example.com/link1\", \"http://example.com/link2\"]"));
    }

    @Test
    public void links_ShouldReturnBadRequestOnIOException() throws Exception {
        String url = "http://example.com";

        Mockito.when(linkLister.getLinks(url)).thenThrow(new IOException());

        mockMvc.perform(get("/links")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        String url = "http://example.com";
        List<String> links = Arrays.asList("http://example.com/link1", "http://example.com/link2");

        Mockito.when(linkLister.getLinksV2(url)).thenReturn(links);

        mockMvc.perform(get("/links-v2")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"http://example.com/link1\", \"http://example.com/link2\"]"));
    }

    @Test
    public void linksV2_ShouldReturnBadRequestOnBadRequestException() throws Exception {
        String url = "http://example.com";

        Mockito.when(linkLister.getLinksV2(url)).thenThrow(new BadRequest());

        mockMvc.perform(get("/links-v2")
                .param("url", url)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
