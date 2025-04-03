package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LinksControllerTests {

    @Autowired
    private LinksController linksController;

    @MockBean
    private LinkLister linkLister;

    // Test for /links endpoint
    @Test
    public void links_ShouldReturnLinks() throws Exception {
        // Mocking the LinkLister.getLinks method
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/page1", "http://example.com/page2");
        Mockito.when(linkLister.getLinks(testUrl)).thenReturn(mockLinks);

        // Calling the controller method
        List<String> result = linksController.links(testUrl);

        // Assertions
        assertEquals("Expected links size does not match", mockLinks.size(), result.size());
        assertTrue("Expected links content does not match", result.containsAll(mockLinks));
    }

    // Test for /links-v2 endpoint
    @Test
    public void linksV2_ShouldReturnLinks() throws Exception {
        // Mocking the LinkLister.getLinksV2 method
        String testUrl = "http://example.com";
        List<String> mockLinks = Arrays.asList("http://example.com/page1", "http://example.com/page2");
        Mockito.when(linkLister.getLinksV2(testUrl)).thenReturn(mockLinks);

        // Calling the controller method
        List<String> result = linksController.linksV2(testUrl);

        // Assertions
        assertEquals("Expected links size does not match", mockLinks.size(), result.size());
        assertTrue("Expected links content does not match", result.containsAll(mockLinks));
    }

    // Test for /links endpoint with invalid URL
    @Test(expected = IOException.class)
    public void links_ShouldThrowIOExceptionForInvalidUrl() throws Exception {
        // Mocking the LinkLister.getLinks method to throw IOException
        String invalidUrl = "invalid-url";
        Mockito.when(linkLister.getLinks(invalidUrl)).thenThrow(new IOException("Invalid URL"));

        // Calling the controller method
        linksController.links(invalidUrl);
    }

    // Test for /links-v2 endpoint with invalid URL
    @Test(expected = BadRequest.class)
    public void linksV2_ShouldThrowBadRequestForInvalidUrl() throws Exception {
        // Mocking the LinkLister.getLinksV2 method to throw BadRequest
        String invalidUrl = "invalid-url";
        Mockito.when(linkLister.getLinksV2(invalidUrl)).thenThrow(new BadRequest("Invalid URL"));

        // Calling the controller method
        linksController.linksV2(invalidUrl);
    }
}

