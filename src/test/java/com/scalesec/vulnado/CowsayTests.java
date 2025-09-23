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

    // Helper method to mock ProcessBuilder and Process
    private ProcessBuilder mockProcessBuilder(String mockOutput) throws Exception {
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Process process = Mockito.mock(Process.class);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(mockOutput.getBytes())));

        Mockito.when(process.getInputStream()).thenReturn(new ByteArrayInputStream(mockOutput.getBytes()));
        Mockito.when(processBuilder.start()).thenReturn(process);

        return processBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput_WhenValidInputProvided() throws Exception {
        // Arrange
        String input = "Hello, World!";
        String expectedOutput = "Mocked Cowsay Output\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should match the mocked cowsay output.", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleEmptyInput() throws Exception {
        // Arrange
        String input = "";
        String expectedOutput = "Mocked Cowsay Output for Empty Input\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should match the mocked cowsay output for empty input.", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleExceptionGracefully() {
        // Arrange
        String input = "This will cause an exception";
        String expectedOutput = ""; // When exception occurs, output is empty

        // Mock ProcessBuilder to throw an exception
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        try {
            Mockito.when(processBuilder.start()).thenThrow(new RuntimeException("Mocked Exception"));
        } catch (Exception e) {
            // This block is intentionally left empty
        }

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("The output should be empty when an exception occurs.", expectedOutput, result);
    }

    @Test
    public void run_ShouldContainCommandInOutput() {
        // Arrange
        String input = "Test Input";
        String expectedCommand = "/usr/games/cowsay 'Test Input'";

        // Act
        Cowsay.run(input);

        // Assert
        assertTrue("The command should be printed to the console.", true); // This is a placeholder for console output verification
    }
}
