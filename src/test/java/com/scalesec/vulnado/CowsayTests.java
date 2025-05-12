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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CowsayTests {

    @Test
    public void run_ShouldReturnCowsayOutput() throws Exception {
        // Arrange
        String input = "Hello, World!";
        String expectedOutput = "  _________\n< Hello, World! >\n  ---------\n        \\   ^__^\n         \\  (oo)\\_______\n            (__)\\       )\\/\\\n                ||----w |\n                ||     ||\n";
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        Process process = mock(Process.class);
        InputStream inputStream = new ByteArrayInputStream(expectedOutput.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        when(processBuilder.start()).thenReturn(process);
        when(process.getInputStream()).thenReturn(inputStream);

        // Act
        String actualOutput = Cowsay.run(input);

        // Assert
        assertEquals("The output should match the expected cowsay output", expectedOutput, actualOutput);
    }

    @Test
    public void run_ShouldHandleException() {
        // Arrange
        String input = "Hello, World!";
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);

        try {
            when(processBuilder.start()).thenThrow(new Exception("Test Exception"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Act
        String actualOutput = Cowsay.run(input);

        // Assert
        assertEquals("The output should be empty when an exception occurs", "", actualOutput);
    }
}
