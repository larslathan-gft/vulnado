package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockStatic;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowControllerTests {

    @InjectMocks
    private CowController cowController;

    // Test for default input
    @Test
    public void cowsay_DefaultInput_ShouldReturnDefaultMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Arrange
            String defaultInput = "I love Linux!";
            String expectedOutput = "Mocked Cowsay Output";
            mockedCowsay.when(() -> Cowsay.run(defaultInput)).thenReturn(expectedOutput);

            // Act
            String result = cowController.cowsay(defaultInput);

            // Assert
            assertEquals("The output should match the mocked Cowsay output for default input.", expectedOutput, result);
        }
    }

    // Test for custom input
    @Test
    public void cowsay_CustomInput_ShouldReturnCustomMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Arrange
            String customInput = "Hello, World!";
            String expectedOutput = "Mocked Cowsay Output for Custom Input";
            mockedCowsay.when(() -> Cowsay.run(customInput)).thenReturn(expectedOutput);

            // Act
            String result = cowController.cowsay(customInput);

            // Assert
            assertEquals("The output should match the mocked Cowsay output for custom input.", expectedOutput, result);
        }
    }

    // Test for empty input
    @Test
    public void cowsay_EmptyInput_ShouldReturnDefaultMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Arrange
            String emptyInput = "";
            String expectedOutput = "Mocked Cowsay Output for Empty Input";
            mockedCowsay.when(() -> Cowsay.run(emptyInput)).thenReturn(expectedOutput);

            // Act
            String result = cowController.cowsay(emptyInput);

            // Assert
            assertEquals("The output should match the mocked Cowsay output for empty input.", expectedOutput, result);
        }
    }

    // Test for null input
    @Test
    public void cowsay_NullInput_ShouldReturnDefaultMessage() {
        try (MockedStatic<Cowsay> mockedCowsay = mockStatic(Cowsay.class)) {
            // Arrange
            String nullInput = null;
            String expectedOutput = "Mocked Cowsay Output for Null Input";
            mockedCowsay.when(() -> Cowsay.run(nullInput)).thenReturn(expectedOutput);

            // Act
            String result = cowController.cowsay(nullInput);

            // Assert
            assertEquals("The output should match the mocked Cowsay output for null input.", expectedOutput, result);
        }
    }
}
