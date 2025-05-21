package com.scalesec.vulnado;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinkListerTests {

    @Test
    public void getLinks_ShouldReturnLinks() throws IOException {
        // Mocking Jsoup Document and Elements
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        // Mocking the behavior of Jsoup.connect(url).get()
        Mockito.when(Jsoup.connect(Mockito.anyString()).get()).thenReturn(mockDocument);
        Mockito.when(mockDocument.select("a")).thenReturn(mockElements);
        Mockito.when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        Mockito.when(mockElement.absUrl("href")).thenReturn("http://example.com");

        // Calling the method
        List<String> links = LinkLister.getLinks("http://test.com");

        // Asserting the result
        assertEquals(1, links.size());
        assertEquals("http://example.com", links.get(0));
    }

    @Test
    public void getLinks_ShouldHandleEmptyLinks() throws IOException {
        // Mocking Jsoup Document and Elements
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);

        // Mocking the behavior of Jsoup.connect(url).get()
        Mockito.when(Jsoup.connect(Mockito.anyString()).get()).thenReturn(mockDocument);
        Mockito.when(mockDocument.select("a")).thenReturn(mockElements);
        Mockito.when(mockElements.iterator()).thenReturn(new ArrayList<Element>().iterator());

        // Calling the method
        List<String> links = LinkLister.getLinks("http://test.com");

        // Asserting the result
        assertEquals(0, links.size());
    }

    @Test
    public void getLinksV2_ShouldReturnLinks() throws IOException, BadRequest {
        // Mocking Jsoup Document and Elements
        Document mockDocument = mock(Document.class);
        Elements mockElements = mock(Elements.class);
        Element mockElement = mock(Element.class);

        // Mocking the behavior of Jsoup.connect(url).get()
        Mockito.when(Jsoup.connect(Mockito.anyString()).get()).thenReturn(mockDocument);
        Mockito.when(mockDocument.select("a")).thenReturn(mockElements);
        Mockito.when(mockElements.iterator()).thenReturn(List.of(mockElement).iterator());
        Mockito.when(mockElement.absUrl("href")).thenReturn("http://example.com");

        // Calling the method
        List<String> links = LinkLister.getLinksV2("http://test.com");

        // Asserting the result
        assertEquals(1, links.size());
        assertEquals("http://example.com", links.get(0));
    }

    @Test
    public void getLinksV2_ShouldThrowBadRequestForPrivateIP() {
        // Asserting the exception
        BadRequest exception = assertThrows(BadRequest.class, () -> {
            LinkLister.getLinksV2("http://192.168.0.1");
        });

        // Asserting the exception message
        assertEquals("Use of Private IP", exception.getMessage());
    }

    @Test
    public void getLinksV2_ShouldThrowBadRequestForInvalidURL() {
        // Asserting the exception
        BadRequest exception = assertThrows(BadRequest.class, () -> {
            LinkLister.getLinksV2("invalid-url");
        });

        // Asserting the exception message
        assertEquals("no protocol: invalid-url", exception.getMessage());
    }
}

// Existing Tests
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }
}
