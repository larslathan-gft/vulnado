package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowsayTests {

    // Helper method to mock ProcessBuilder and Process
    private ProcessBuilder mockProcessBuilder(String mockOutput) throws Exception {
        ProcessBuilder mockProcessBuilder = mock(ProcessBuilder.class);
        Process mockProcess = mock(Process.class);
        InputStream mockInputStream = new ByteArrayInputStream(mockOutput.getBytes());
        when(mockProcess.getInputStream()).thenReturn(mockInputStream);
        when(mockProcessBuilder.start()).thenReturn(mockProcess);
        return mockProcessBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput_WhenValidInputProvided() throws Exception {
        // Arrange
        String input = "Hello, World!";
        String expectedOutput = "Mocked Cowsay Output\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(expectedOutput);
        Cowsay cowsay = Mockito.spy(new Cowsay());
        doReturn(mockProcessBuilder).when(cowsay).createProcessBuilder();

        // Act
        String result = cowsay.run(input);

        // Assert
        assertEquals("The output should match the mocked cowsay output", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleException_WhenProcessFails() throws Exception {
        // Arrange
        String input = "Hello, World!";
        ProcessBuilder mockProcessBuilder = mock(ProcessBuilder.class);
        when(mockProcessBuilder.start()).thenThrow(new RuntimeException("Mocked Exception"));
        Cowsay cowsay = Mockito.spy(new Cowsay());
        doReturn(mockProcessBuilder).when(cowsay).createProcessBuilder();

        // Act
        String result = cowsay.run(input);

        // Assert
        assertTrue("The output should be empty when an exception occurs", result.isEmpty());
    }

    @Test
    public void run_ShouldReturnEmptyOutput_WhenNoInputProvided() throws Exception {
        // Arrange
        String input = "";
        String expectedOutput = "";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(expectedOutput);
        Cowsay cowsay = Mockito.spy(new Cowsay());
        doReturn(mockProcessBuilder).when(cowsay).createProcessBuilder();

        // Act
        String result = cowsay.run(input);

        // Assert
        assertEquals("The output should be empty when no input is provided", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleMultilineOutput() throws Exception {
        // Arrange
        String input = "Multiline Test";
        String expectedOutput = "Line 1\nLine 2\nLine 3\n";
        ProcessBuilder mockProcessBuilder = mockProcessBuilder(expectedOutput);
        Cowsay cowsay = Mockito.spy(new Cowsay());
        doReturn(mockProcessBuilder).when(cowsay).createProcessBuilder();

        // Act
        String result = cowsay.run(input);

        // Assert
        assertEquals("The output should match the mocked multiline output", expectedOutput, result);
    }
}
