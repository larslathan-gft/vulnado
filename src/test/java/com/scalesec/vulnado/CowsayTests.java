package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    // Helper method to mock ProcessBuilder and Process
    private ProcessBuilder mockProcessBuilder(String input, String output) throws Exception {
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        Process process = Mockito.mock(Process.class);
        InputStream inputStream = new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        when(processBuilder.command("bash", "-c", "/usr/games/cowsay '" + input + "'")).thenReturn(processBuilder);
        when(processBuilder.start()).thenReturn(process);
        when(process.getInputStream()).thenReturn(inputStream);

        return processBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput() throws Exception {
        String input = "Hello, World!";
        String expectedOutput = "  _______\n< Hello, World! >\n  -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();
        
        String result = cowsay.run(input);
        
        assertEquals("The output should match the expected cowsay output", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleEmptyInput() throws Exception {
        String input = "";
        String expectedOutput = "  _______\n<  >\n  -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();
        
        String result = cowsay.run(input);
        
        assertEquals("The output should match the expected cowsay output for empty input", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleSpecialCharacters() throws Exception {
        String input = "Special characters: !@#$%^&*()";
        String expectedOutput = "  ___________________________\n< Special characters: !@#$%^&*() >\n  ---------------------------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();
        
        String result = cowsay.run(input);
        
        assertEquals("The output should match the expected cowsay output for special characters", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleException() throws Exception {
        String input = "Exception test";
        ProcessBuilder processBuilder = Mockito.mock(ProcessBuilder.class);
        when(processBuilder.command("bash", "-c", "/usr/games/cowsay '" + input + "'")).thenReturn(processBuilder);
        when(processBuilder.start()).thenThrow(new RuntimeException("Test exception"));

        Cowsay cowsay = new Cowsay();
        
        String result = cowsay.run(input);
        
        assertEquals("The output should be empty when an exception occurs", "", result);
    }
}
