import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    @Test
    public void getLinks_ShouldReturnLinks() throws IOException {
        // Arrange
        String url = "http://example.com";
        Document mockDocument = mock(Document.class);
        Elements mockElements = new Elements();
        Element mockElement = mock(Element.class);
        mockElements.add(mockElement);

        when(mockElement.absUrl("href")).thenReturn("http://example.com/link");
        when(mockDocument.select("a")).thenReturn(mockElements);
        mockJsoupConnect(url, mockDocument);

        // Act
        List<String> result = LinkLister.getLinks(url);

        // Assert
        assertEquals(1, result.size());
        assertEquals("http://example.com/link", result.get(0));
    }

    @Test
    public void getLinks_ShouldReturnEmptyListWhenNoLinks() throws IOException {
        // Arrange
        String url = "http://example.com";
        Document mockDocument = mock(Document.class);
        Elements mockElements = new Elements();

        when(mockDocument.select("a")).thenReturn(mockElements);
        mockJsoupConnect(url, mockDocument);

        // Act
        List<String> result = LinkLister.getLinks(url);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test(expected = IOException.class)
    public void getLinks_ShouldThrowIOException() throws IOException {
        // Arrange
        String url = "http://example.com";
        mockJsoupConnectThrows(url, new IOException());

        // Act
        LinkLister.getLinks(url);
    }

    @Test
    public void getLinksV2_ShouldReturnLinks() throws IOException, BadRequest {
        // Arrange
        String url = "http://example.com";
        Document mockDocument = mock(Document.class);
        Elements mockElements = new Elements();
        Element mockElement = mock(Element.class);
        mockElements.add(mockElement);

        when(mockElement.absUrl("href")).thenReturn("http://example.com/link");
        when(mockDocument.select("a")).thenReturn(mockElements);
        mockJsoupConnect(url, mockDocument);

        // Act
        List<String> result = LinkLister.getLinksV2(url);

        // Assert
        assertEquals(1, result.size());
        assertEquals("http://example.com/link", result.get(0));
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForPrivateIP() throws BadRequest {
        // Arrange
        String url = "http://192.168.0.1";

        // Act
        LinkLister.getLinksV2(url);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForInvalidURL() throws BadRequest {
        // Arrange
        String url = "invalid-url";

        // Act
        LinkLister.getLinksV2(url);
    }

    private void mockJsoupConnect(String url, Document document) throws IOException {
        Jsoup jsoup = mock(Jsoup.class);
        when(Jsoup.connect(url)).thenReturn(jsoup);
        when(jsoup.get()).thenReturn(document);
    }

    private void mockJsoupConnectThrows(String url, IOException exception) throws IOException {
        Jsoup jsoup = mock(Jsoup.class);
        when(Jsoup.connect(url)).thenReturn(jsoup);
        when(jsoup.get()).thenThrow(exception);
    }
}
