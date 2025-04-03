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

    // Test for getLinks method
    @Test
    public void getLinks_ValidUrl_ShouldReturnLinks() throws IOException {
        // Mock Jsoup and Document
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        when(mockElement.absUrl("href")).thenReturn("http://example.com");
        when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);
        Mockito.mockStatic(Jsoup.class).when(() -> Jsoup.connect("http://test.com").get()).thenReturn(mockDocument);

        List<String> links = LinkLister.getLinks("http://test.com");

        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 1", 1, links.size());
        assertEquals("Link should match expected URL", "http://example.com", links.get(0));
    }

    @Test(expected = IOException.class)
    public void getLinks_InvalidUrl_ShouldThrowIOException() throws IOException {
        // Mock Jsoup to throw IOException
        Mockito.mockStatic(Jsoup.class).when(() -> Jsoup.connect("http://invalid-url.com").get()).thenThrow(new IOException());

        LinkLister.getLinks("http://invalid-url.com");
    }

    // Test for getLinksV2 method
    @Test
    public void getLinksV2_ValidUrl_ShouldReturnLinks() throws BadRequest, IOException {
        // Mock Jsoup and Document
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        when(mockElement.absUrl("href")).thenReturn("http://example.com");
        when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        when(mockDocument.select("a")).thenReturn(mockElements);
        Mockito.mockStatic(Jsoup.class).when(() -> Jsoup.connect("http://test.com").get()).thenReturn(mockDocument);

        List<String> links = LinkLister.getLinksV2("http://test.com");

        assertNotNull("Links should not be null", links);
        assertEquals("Links size should be 1", 1, links.size());
        assertEquals("Link should match expected URL", "http://example.com", links.get(0));
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_PrivateIp_ShouldThrowBadRequest() throws BadRequest {
        LinkLister.getLinksV2("http://192.168.1.1");
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_InvalidUrl_ShouldThrowBadRequest() throws BadRequest {
        LinkLister.getLinksV2("invalid-url");
    }

    @Test
    public void getLinksV2_ValidUrl_ShouldHandleExceptionGracefully() {
        try {
            LinkLister.getLinksV2("http://test.com");
        } catch (BadRequest e) {
            fail("Should not throw BadRequest for valid URL");
        }
    }
}

