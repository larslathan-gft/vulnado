package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowControllerTests {

    @InjectMocks
    private CowController cowController;

    @Mock
    private Cowsay cowsay;

    @Test
    public void cowsay_ShouldReturnDefaultMessage() {
        // Arrange
        String expected = "I love Linux!";
        Mockito.when(cowsay.run(expected)).thenReturn(expected);

        // Act
        String result = cowController.cowsay(expected);

        // Assert
        assertEquals("The default message should be 'I love Linux!'", expected, result);
    }

    @Test
    public void cowsay_ShouldReturnCustomMessage() {
        // Arrange
        String input = "Hello, World!";
        String expected = "Hello, World!";
        Mockito.when(cowsay.run(input)).thenReturn(expected);

        // Act
        String result = cowController.cowsay(input);

        // Assert
        assertEquals("The custom message should be 'Hello, World!'", expected, result);
    }

    @Test
    public void cowsay_ShouldHandleEmptyInput() {
        // Arrange
        String input = "";
        String expected = "";
        Mockito.when(cowsay.run(input)).thenReturn(expected);

        // Act
        String result = cowController.cowsay(input);

        // Assert
        assertEquals("The message should be empty", expected, result);
    }

    @Test
    public void cowsay_ShouldHandleNullInput() {
        // Arrange
        String input = null;
        String expected = "I love Linux!";
        Mockito.when(cowsay.run(expected)).thenReturn(expected);

        // Act
        String result = cowController.cowsay(input);

        // Assert
        assertEquals("The message should be 'I love Linux!' when input is null", expected, result);
    }
}

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
