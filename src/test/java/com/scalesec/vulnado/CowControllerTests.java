package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CowControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void cowsay_ShouldReturnDefaultMessage() {
        // Arrange
        String expected = Cowsay.run("I love Linux!");

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay", String.class);

        // Assert
        assertEquals("Default message should be 'I love Linux!'", expected, response.getBody());
    }

    @Test
    public void cowsay_ShouldReturnCustomMessage() {
        // Arrange
        String customMessage = "Hello, World!";
        String expected = Cowsay.run(customMessage);

        // Act
        ResponseEntity<String> response = restTemplate.getForEntity("/cowsay?input=" + customMessage, String.class);

        // Assert
        assertEquals("Custom message should be 'Hello, World!'", expected, response.getBody());
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
