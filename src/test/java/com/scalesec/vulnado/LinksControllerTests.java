import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Mock
    private LinkLister linkLister;

    @InjectMocks
    private LinksController linksController;

    @Test
    public void links_ShouldReturnListOfLinks() throws IOException {
        // Arrange
        List<String> expectedLinks = Arrays.asList("http://example.com", "http://example.org");
        Mockito.when(linkLister.getLinks(anyString())).thenReturn(expectedLinks);

        // Act
        List<String> actualLinks = linksController.links("http://test.com");

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);
    }

    @Test
    public void links_ShouldThrowIOException() throws IOException {
        // Arrange
        Mockito.when(linkLister.getLinks(anyString())).thenThrow(new IOException("IO Exception"));

        // Act & Assert
        assertThrows("Should throw IOException", IOException.class, () -> linksController.links("http://test.com"));
    }

    @Test
    public void linksV2_ShouldReturnListOfLinks() throws BadRequest {
        // Arrange
        List<String> expectedLinks = Arrays.asList("http://example.com", "http://example.org");
        Mockito.when(linkLister.getLinksV2(anyString())).thenReturn(expectedLinks);

        // Act
        List<String> actualLinks = linksController.linksV2("http://test.com");

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);
    }

    @Test
    public void linksV2_ShouldThrowBadRequest() throws BadRequest {
        // Arrange
        Mockito.when(linkLister.getLinksV2(anyString())).thenThrow(new BadRequest("Bad Request"));

        // Act & Assert
        assertThrows("Should throw BadRequest", BadRequest.class, () -> linksController.linksV2("http://test.com"));
    }
}
