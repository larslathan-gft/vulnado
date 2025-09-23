package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowsayTests {

    // Helper method to mock ProcessBuilder and its behavior
    private ProcessBuilder mockProcessBuilder(String input, String mockOutput) throws Exception {
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Process process = Mockito.mock(Process.class);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(mockOutput.getBytes())));

        Mockito.when(processBuilder.start()).thenReturn(process);
        Mockito.when(process.getInputStream()).thenReturn(new ByteArrayInputStream(mockOutput.getBytes()));

        return processBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput() throws Exception {
        // Arrange
        String input = "Hello, World!";
        String expectedOutput = " _______\n< Hello, World! >\n -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should match the expected cowsay output", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleEmptyInput() throws Exception {
        // Arrange
        String input = "";
        String expectedOutput = " _______\n<  >\n -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should handle empty input gracefully", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleSpecialCharacters() throws Exception {
        // Arrange
        String input = "Special! @#$%^&*()";
        String expectedOutput = " _______\n< Special! @#$%^&*() >\n -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should handle special characters correctly", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleExceptionGracefully() {
        // Arrange
        String input = "Exception Test";
        String expectedOutput = "";
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);

        try {
            Mockito.when(processBuilder.start()).thenThrow(new RuntimeException("Mocked Exception"));
        } catch (Exception e) {
            // Ignored for mocking
        }

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should be empty when an exception occurs", expectedOutput, result);
    }
}
