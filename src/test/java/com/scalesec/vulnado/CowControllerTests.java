package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private Cowsay cowsay;

    @Test
    public void cowsay_WithDefaultInput_ShouldReturnDefaultMessage() {
        // Arrange
        String defaultMessage = "I love Linux!";
        String expectedOutput = "Mocked Cowsay Output for Default Message";
        when(cowsay.run(defaultMessage)).thenReturn(expectedOutput);

        CowController controller = new CowController();

        // Act
        String result = controller.cowsay(defaultMessage);

        // Assert
        assertEquals("The output should match the mocked Cowsay output for the default message.", expectedOutput, result);
    }

    @Test
    public void cowsay_WithCustomInput_ShouldReturnCustomMessage() {
        // Arrange
        String customMessage = "Hello, World!";
        String expectedOutput = "Mocked Cowsay Output for Custom Message";
        when(cowsay.run(customMessage)).thenReturn(expectedOutput);

        CowController controller = new CowController();

        // Act
        String result = controller.cowsay(customMessage);

        // Assert
        assertEquals("The output should match the mocked Cowsay output for the custom message.", expectedOutput, result);
    }

    @Test
    public void cowsay_WithEmptyInput_ShouldReturnDefaultMessage() {
        // Arrange
        String emptyInput = "";
        String defaultMessage = "I love Linux!";
        String expectedOutput = "Mocked Cowsay Output for Default Message";
        when(cowsay.run(defaultMessage)).thenReturn(expectedOutput);

        CowController controller = new CowController();

        // Act
        String result = controller.cowsay(emptyInput);

        // Assert
        assertEquals("The output should match the mocked Cowsay output for the default message when input is empty.", expectedOutput, result);
    }
}
