package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LinksControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // Mocking LinkLister to isolate the controller logic
    private static LinkLister mockLinkLister = mock(LinkLister.class);

    @Test
    public void links_ValidUrl_ShouldReturnLinks() throws IOException {
        // Arrange
        String url = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(mockLinkLister.getLinks(url)).thenReturn(expectedLinks);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(createURLWithPort("/links?url=" + url), List.class);

        // Assert
        assertEquals("Expected HTTP status 200", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());
        assertEquals("Expected links to match", expectedLinks, response.getBody());
    }

    @Test
    public void links_InvalidUrl_ShouldReturnEmptyList() throws IOException {
        // Arrange
        String url = "invalid-url";
        when(mockLinkLister.getLinks(url)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(createURLWithPort("/links?url=" + url), List.class);

        // Assert
        assertEquals("Expected HTTP status 200", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());
        assertEquals("Expected empty list for invalid URL", Collections.emptyList(), response.getBody());
    }

    @Test
    public void linksV2_ValidUrl_ShouldReturnLinks() throws BadRequest {
        // Arrange
        String url = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(mockLinkLister.getLinksV2(url)).thenReturn(expectedLinks);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(createURLWithPort("/links-v2?url=" + url), List.class);

        // Assert
        assertEquals("Expected HTTP status 200", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());
        assertEquals("Expected links to match", expectedLinks, response.getBody());
    }

    @Test
    public void linksV2_InvalidUrl_ShouldReturnBadRequest() throws BadRequest {
        // Arrange
        String url = "invalid-url";
        when(mockLinkLister.getLinksV2(url)).thenThrow(new BadRequest("Invalid URL"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/links-v2?url=" + url), String.class);

        // Assert
        assertEquals("Expected HTTP status 400", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());
        assertEquals("Expected error message for invalid URL", "Invalid URL", response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
