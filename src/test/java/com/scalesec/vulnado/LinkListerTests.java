import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Helper method to create a mock Document with given links
    private Document createMockDocumentWithLinks(List<String> links) {
        Document mockDocument = mock(Document.class);
        Elements mockElements = new Elements();
        for (String link : links) {
            Element mockElement = mock(Element.class);
            when(mockElement.absUrl("href")).thenReturn(link);
            mockElements.add(mockElement);
        }
        when(mockDocument.select("a")).thenReturn(mockElements);
        return mockDocument;
    }

    @Test
    public void getLinks_ShouldReturnAllLinks() throws IOException {
        // Arrange
        String url = "http://example.com";
        List<String> expectedLinks = List.of("http://example.com/page1", "http://example.com/page2");
        Document mockDocument = createMockDocumentWithLinks(expectedLinks);
        Jsoup jsoupMock = mock(Jsoup.class);
        when(jsoupMock.connect(url)).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinks(url);

        // Assert
        assertEquals("The links should match the expected links", expectedLinks, actualLinks);
    }

    @Test
    public void getLinks_ShouldHandleEmptyLinks() throws IOException {
        // Arrange
        String url = "http://example.com";
        List<String> expectedLinks = new ArrayList<>();
        Document mockDocument = createMockDocumentWithLinks(expectedLinks);
        Jsoup jsoupMock = mock(Jsoup.class);
        when(jsoupMock.connect(url)).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinks(url);

        // Assert
        assertEquals("The links should be empty", expectedLinks, actualLinks);
    }

    @Test
    public void getLinksV2_ShouldReturnAllLinks() throws IOException, BadRequest {
        // Arrange
        String url = "http://example.com";
        List<String> expectedLinks = List.of("http://example.com/page1", "http://example.com/page2");
        Document mockDocument = createMockDocumentWithLinks(expectedLinks);
        Jsoup jsoupMock = mock(Jsoup.class);
        when(jsoupMock.connect(url)).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinksV2(url);

        // Assert
        assertEquals("The links should match the expected links", expectedLinks, actualLinks);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForPrivateIP() throws BadRequest {
        // Arrange
        String url = "http://192.168.0.1";

        // Act
        LinkLister.getLinksV2(url);

        // Assert
        // Expect BadRequest exception
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForInvalidURL() throws BadRequest {
        // Arrange
        String url = "invalid-url";

        // Act
        LinkLister.getLinksV2(url);

        // Assert
        // Expect BadRequest exception
    }
}
