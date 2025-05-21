import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.lang.Process;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowsayTests {

    // Helper method to mock ProcessBuilder and Process
    private ProcessBuilder mockProcessBuilder(String input, String output) throws IOException {
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        Process process = mock(Process.class);
        BufferedReader reader = new BufferedReader(new StringReader(output));

        when(processBuilder.command(anyString(), anyString(), anyString())).thenReturn(processBuilder);
        when(processBuilder.start()).thenReturn(process);
        when(process.getInputStream()).thenReturn(new InputStreamReader(reader).getInputStream());

        return processBuilder;
    }

    @Test
    public void run_ShouldReturnCowsayOutput() throws IOException {
        // Arrange
        String input = "Hello, World!";
        String expectedOutput = "  _______\n< Hello, World! >\n  -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("Cowsay output should match expected output", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleEmptyInput() throws IOException {
        // Arrange
        String input = "";
        String expectedOutput = "  _______\n<  >\n  -------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("Cowsay output should handle empty input", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleSpecialCharacters() throws IOException {
        // Arrange
        String input = "Special characters: !@#$%^&*()";
        String expectedOutput = "  ___________________________\n< Special characters: !@#$%^&*() >\n  ---------------------------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("Cowsay output should handle special characters", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleLongInput() throws IOException {
        // Arrange
        String input = "This is a very long input string to test how cowsay handles it. It should be able to handle it gracefully without any issues.";
        String expectedOutput = "  ___________________________________________________________________________________________\n< This is a very long input string to test how cowsay handles it. It should be able to handle it gracefully without any issues. >\n  -----------------------------------------------------------------------------------------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder processBuilder = mockProcessBuilder(input, expectedOutput);

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("Cowsay output should handle long input", expectedOutput, result);
    }

    @Test
    public void run_ShouldHandleException() throws IOException {
        // Arrange
        String input = "Exception test";
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        when(processBuilder.command(anyString(), anyString(), anyString())).thenReturn(processBuilder);
        when(processBuilder.start()).thenThrow(new IOException("Test exception"));

        // Act
        String result = Cowsay.run(input);

        // Assert
        assertEquals("Cowsay output should handle exceptions gracefully", "", result);
    }
}
