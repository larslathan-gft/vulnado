package com.scalesec.vulnado;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Helper method to mock Jsoup Document and Elements
    private Document mockJsoupDocumentWithLinks(List<String> links) {
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        List<Element> mockElementList = new ArrayList<>();

        for (String link : links) {
            Element mockElement = mock(Element.class);
            when(mockElement.absUrl("href")).thenReturn(link);
            mockElementList.add(mockElement);
        }

        when(mockElements.iterator()).thenReturn(mockElementList.iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);

        return mockDocument;
    }

    @Test
    public void getLinks_ShouldReturnListOfLinks() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = List.of("http://example.com/page1", "http://example.com/page2");
        Document mockDocument = mockJsoupDocumentWithLinks(expectedLinks);

        // Mock Jsoup.connect().get() behavior
        Mockito.mockStatic(Jsoup.class);
        when(Jsoup.connect(testUrl).get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinks(testUrl);

        // Assert
        assertEquals("The returned list of links should match the expected list.", expectedLinks, actualLinks);

        // Cleanup
        Mockito.clearAllCaches();
    }

    @Test(expected = IOException.class)
    public void getLinks_ShouldThrowIOException_WhenJsoupFails() throws IOException {
        // Arrange
        String testUrl = "http://example.com";

        // Mock Jsoup.connect().get() to throw IOException
        Mockito.mockStatic(Jsoup.class);
        when(Jsoup.connect(testUrl).get()).thenThrow(new IOException("Connection failed"));

        // Act
        LinkLister.getLinks(testUrl);

        // Cleanup
        Mockito.clearAllCaches();
    }

    @Test
    public void getLinksV2_ShouldReturnListOfLinks_WhenUrlIsValid() throws Exception {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = List.of("http://example.com/page1", "http://example.com/page2");
        Document mockDocument = mockJsoupDocumentWithLinks(expectedLinks);

        // Mock Jsoup.connect().get() behavior
        Mockito.mockStatic(Jsoup.class);
        when(Jsoup.connect(testUrl).get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinksV2(testUrl);

        // Assert
        assertEquals("The returned list of links should match the expected list.", expectedLinks, actualLinks);

        // Cleanup
        Mockito.clearAllCaches();
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenUrlIsPrivateIP() throws Exception {
        // Arrange
        String testUrl = "http://192.168.1.1";

        // Act
        LinkLister.getLinksV2(testUrl);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequest_WhenUrlIsInvalid() throws Exception {
        // Arrange
        String testUrl = "invalid-url";

        // Act
        LinkLister.getLinksV2(testUrl);
    }

    @Test
    public void getLinksV2_ShouldThrowBadRequest_WithCorrectMessage_WhenUrlIsPrivateIP() {
        // Arrange
        String testUrl = "http://10.0.0.1";

        try {
            // Act
            LinkLister.getLinksV2(testUrl);
            fail("Expected BadRequest exception was not thrown.");
        } catch (BadRequest e) {
            // Assert
            assertEquals("Exception message should match.", "Use of Private IP", e.getMessage());
        }
    }

    @Test
    public void getLinksV2_ShouldThrowBadRequest_WithCorrectMessage_WhenUrlIsInvalid() {
        // Arrange
        String testUrl = "invalid-url";

        try {
            // Act
            LinkLister.getLinksV2(testUrl);
            fail("Expected BadRequest exception was not thrown.");
        } catch (BadRequest e) {
            // Assert
            assertTrue("Exception message should contain 'no protocol'.", e.getMessage().contains("no protocol"));
        }
    }
}
