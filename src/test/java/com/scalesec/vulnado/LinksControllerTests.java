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
import static org.junit.Assert.assertTrue;
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
    private final LinkLister linkListerMock = mock(LinkLister.class);

    private String getBaseUrl(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

    @Test
    public void links_ShouldReturnLinks_WhenValidUrlProvided() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(linkListerMock.getLinks(testUrl)).thenReturn(expectedLinks);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(getBaseUrl("/links?url=" + testUrl), List.class);

        // Assert
        assertEquals("Expected HTTP status 200", HttpStatus.OK, response.getStatusCode());
        assertEquals("Expected links to match", expectedLinks, response.getBody());
    }

    @Test
    public void links_ShouldReturnEmptyList_WhenNoLinksFound() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        when(linkListerMock.getLinks(testUrl)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(getBaseUrl("/links?url=" + testUrl), List.class);

        // Assert
        assertEquals("Expected HTTP status 200", HttpStatus.OK, response.getStatusCode());
        assertTrue("Expected empty list of links", response.getBody().isEmpty());
    }

    @Test
    public void links_ShouldReturnError_WhenIOExceptionOccurs() throws IOException {
        // Arrange
        String testUrl = "http://example.com";
        when(linkListerMock.getLinks(testUrl)).thenThrow(new IOException("Test IOException"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl("/links?url=" + testUrl), String.class);

        // Assert
        assertEquals("Expected HTTP status 500", HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue("Expected error message to contain 'Test IOException'", response.getBody().contains("Test IOException"));
    }

    @Test
    public void linksV2_ShouldReturnLinks_WhenValidUrlProvided() throws BadRequest {
        // Arrange
        String testUrl = "http://example.com";
        List<String> expectedLinks = Arrays.asList("http://example.com/link1", "http://example.com/link2");
        when(linkListerMock.getLinksV2(testUrl)).thenReturn(expectedLinks);

        // Act
        ResponseEntity<List> response = restTemplate.getForEntity(getBaseUrl("/links-v2?url=" + testUrl), List.class);

        // Assert
        assertEquals("Expected HTTP status 200", HttpStatus.OK, response.getStatusCode());
        assertEquals("Expected links to match", expectedLinks, response.getBody());
    }

    @Test
    public void linksV2_ShouldReturnError_WhenBadRequestOccurs() throws BadRequest {
        // Arrange
        String testUrl = "http://example.com";
        when(linkListerMock.getLinksV2(testUrl)).thenThrow(new BadRequest("Test BadRequest"));

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity(getBaseUrl("/links-v2?url=" + testUrl), String.class);

        // Assert
        assertEquals("Expected HTTP status 400", HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue("Expected error message to contain 'Test BadRequest'", response.getBody().contains("Test BadRequest"));
    }
}
