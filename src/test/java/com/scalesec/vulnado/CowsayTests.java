//BEGIN: Work/DemoTestCreator/2025-06-13__11-55-15.953__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    // Helper method to mock ProcessBuilder and Process
    private ProcessBuilder mockProcessBuilder(String input, String output) throws Exception {
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        Process process = mock(Process.class);
        BufferedReader reader = new BufferedReader(new StringReader(output));

        when(processBuilder.command(anyString(), anyString(), anyString())).thenReturn(processBuilder);
        when(processBuilder.start()).thenReturn(process);
        when(process.getInputStream()).thenReturn(new InputStreamReader(reader).getInputStream());

        return processBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput() throws Exception {
        String input = "Hello, World!";
        String expectedOutput = "  _______\n< Hello, World! >\n  -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";

        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();

        String result = cowsay.run(input);

        assertEquals("Cowsay output should match expected output", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleEmptyInput() throws Exception {
        String input = "";
        String expectedOutput = "  _______\n<  >\n  -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";

        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();

        String result = cowsay.run(input);

        assertEquals("Cowsay output should handle empty input", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleSpecialCharacters() throws Exception {
        String input = "Special characters: !@#$%^&*()";
        String expectedOutput = "  ___________________________\n< Special characters: !@#$%^&*() >\n  ---------------------------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";

        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();

        String result = cowsay.run(input);

        assertEquals("Cowsay output should handle special characters", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleLongInput() throws Exception {
        String input = "This is a very long input string to test how cowsay handles long inputs. It should wrap the text appropriately.";
        String expectedOutput = "  ___________________________________________________________\n< This is a very long input string to test how cowsay handles >\n< long inputs. It should wrap the text appropriately.         >\n  -----------------------------------------------------------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";

        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);
        Cowsay cowsay = new Cowsay();

        String result = cowsay.run(input);

        assertEquals("Cowsay output should handle long input", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleException() throws Exception {
        String input = "Exception test";
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        when(processBuilder.command(anyString(), anyString(), anyString())).thenReturn(processBuilder);
        when(processBuilder.start()).thenThrow(new RuntimeException("Test exception"));

        Cowsay cowsay = new Cowsay();

        String result = cowsay.run(input);

        assertTrue("Cowsay should handle exceptions gracefully", result.isEmpty());
    }
}
//END: Work/DemoTestCreator/2025-06-13__11-55-15.953__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
