package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

    // Test for the /links endpoint
    @Test
    public void links_ValidUrl_ShouldReturnLinks() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        Mockito.when(linkLister.getLinks(testUrl)).thenReturn(expectedLinks);

        // Act
        List<String> actualLinks = linksController.links(testUrl);

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);
    }

    @Test
    public void links_InvalidUrl_ShouldThrowIOException() {
        // Arrange
        String invalidUrl = "invalid-url";
        Mockito.when(linkLister.getLinks(invalidUrl)).thenThrow(new IOException("Invalid URL"));

        // Act & Assert
        try {
            linksController.links(invalidUrl);
        } catch (IOException e) {
            assertEquals("Exception message should match", "Invalid URL", e.getMessage());
        }
    }

    // Test for the /links-v2 endpoint
    @Test
    public void linksV2_ValidUrl_ShouldReturnLinks() throws BadRequest {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        Mockito.when(linkLister.getLinksV2(testUrl)).thenReturn(expectedLinks);

        // Act
        List<String> actualLinks = linksController.linksV2(testUrl);

        // Assert
        assertEquals("The returned links should match the expected links", expectedLinks, actualLinks);
    }

    @Test
    public void linksV2_InvalidUrl_ShouldThrowBadRequest() {
        // Arrange
        String invalidUrl = "invalid-url";
        Mockito.when(linkLister.getLinksV2(invalidUrl)).thenThrow(new BadRequest("Invalid URL"));

        // Act & Assert
        try {
            linksController.linksV2(invalidUrl);
        } catch (BadRequest e) {
            assertEquals("Exception message should match", "Invalid URL", e.getMessage());
        }
    }

    // Test for empty response
    @Test
    public void links_EmptyResponse_ShouldReturnEmptyList() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        Mockito.when(linkLister.getLinks(testUrl)).thenReturn(Collections.emptyList());

        // Act
        List<String> actualLinks = linksController.links(testUrl);

        // Assert
        assertTrue("The returned list should be empty", actualLinks.isEmpty());
    }

    @Test
    public void linksV2_EmptyResponse_ShouldReturnEmptyList() throws BadRequest {
        // Arrange
        String testUrl = "http://example.com";
        Mockito.when(linkLister.getLinksV2(testUrl)).thenReturn(Collections.emptyList());

        // Act
        List<String> actualLinks = linksController.linksV2(testUrl);

        // Assert
        assertTrue("The returned list should be empty", actualLinks.isEmpty());
    }
}
