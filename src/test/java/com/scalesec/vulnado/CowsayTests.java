package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void cowsay_Run_ShouldReturnExpectedOutput() throws Exception {
        // Mocking the ProcessBuilder and Process
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Process processMock = Mockito.mock(Process.class);

        // Mocking the InputStream of the Process
        String expectedOutput = "expected cowsay output\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedOutput.getBytes(StandardCharsets.UTF_8));
        Mockito.when(processMock.getInputStream()).thenReturn(inputStream);

        // Mocking the start method of ProcessBuilder
        Mockito.when(processBuilderMock.start()).thenReturn(processMock);

        // Injecting the mock into the Cowsay class
        Cowsay cowsay = new Cowsay() {
            @Override
            protected ProcessBuilder createProcessBuilder() {
                return processBuilderMock;
            }
        };

        // Running the test
        String input = "Hello, World!";
        String actualOutput = cowsay.run(input);

        // Asserting the output
        assertEquals("The output should match the expected cowsay output", expectedOutput, actualOutput);
    }

    @Test
    public void cowsay_Run_ShouldHandleException() {
        // Mocking the ProcessBuilder to throw an exception
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Mockito.when(processBuilderMock.start()).thenThrow(new RuntimeException("Test Exception"));

        // Injecting the mock into the Cowsay class
        Cowsay cowsay = new Cowsay() {
            @Override
            protected ProcessBuilder createProcessBuilder() {
                return processBuilderMock;
            }
        };

        // Running the test
        String input = "Hello, World!";
        String actualOutput = cowsay.run(input);

        // Asserting the output
        assertTrue("The output should be empty when an exception occurs", actualOutput.isEmpty());
    }
}
