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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    // Helper method to mock Jsoup Document and Elements
    private Document mockJsoupDocument(List<String> links) {
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
        Document mockDocument = mockJsoupDocument(expectedLinks);

        // Mock Jsoup.connect().get() to return the mocked Document
        Jsoup jsoupMock = mock(Jsoup.class);
        when(Jsoup.connect(testUrl).get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinks(testUrl);

        // Assert
        assertEquals("The returned list of links should match the expected list", expectedLinks, actualLinks);
    }

    @Test(expected = IOException.class)
    public void getLinks_ShouldThrowIOExceptionForInvalidUrl() throws IOException {
        // Arrange
        String invalidUrl = "http://invalid-url";
        when(Jsoup.connect(invalidUrl).get()).thenThrow(new IOException("Invalid URL"));

        // Act
        LinkLister.getLinks(invalidUrl);
    }

    @Test
    public void getLinksV2_ShouldReturnListOfLinksForValidUrl() throws BadRequest, IOException {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = List.of("http://example.com/page1", "http://example.com/page2");
        Document mockDocument = mockJsoupDocument(expectedLinks);

        // Mock Jsoup.connect().get() to return the mocked Document
        Jsoup jsoupMock = mock(Jsoup.class);
        when(Jsoup.connect(testUrl).get()).thenReturn(mockDocument);

        // Act
        List<String> actualLinks = LinkLister.getLinksV2(testUrl);

        // Assert
        assertEquals("The returned list of links should match the expected list", expectedLinks, actualLinks);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForPrivateIp() throws BadRequest {
        // Arrange
        String privateIpUrl = "http://192.168.1.1";

        // Act
        LinkLister.getLinksV2(privateIpUrl);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForMalformedUrl() throws BadRequest {
        // Arrange
        String malformedUrl = "malformed-url";

        // Act
        LinkLister.getLinksV2(malformedUrl);
    }

    @Test
    public void getLinksV2_ShouldHandleExceptionGracefully() {
        // Arrange
        String invalidUrl = "http://invalid-url";
        try {
            // Act
            LinkLister.getLinksV2(invalidUrl);
            fail("Expected BadRequest exception to be thrown");
        } catch (BadRequest e) {
            // Assert
            assertNotNull("Exception message should not be null", e.getMessage());
        }
    }
}
