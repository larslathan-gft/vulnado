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

        // Mocking the input stream of the process to return a predefined output
        String expectedOutput = "expected cowsay output\n";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(expectedOutput.getBytes(StandardCharsets.UTF_8));
        BufferedReader bufferedReaderMock = new BufferedReader(new InputStreamReader(byteArrayInputStream));

        Mockito.when(processBuilderMock.start()).thenReturn(processMock);
        Mockito.when(processMock.getInputStream()).thenReturn(byteArrayInputStream);

        // Injecting the mock into the Cowsay class
        Cowsay cowsay = new Cowsay() {
            @Override
            protected ProcessBuilder createProcessBuilder() {
                return processBuilderMock;
            }
        };

        // Running the method and asserting the output
        String input = "Hello, World!";
        String actualOutput = cowsay.run(input);
        assertEquals("The output should match the expected cowsay output", expectedOutput, actualOutput);
    }

    @Test
    public void cowsay_Run_ShouldHandleException() {
        // Mocking the ProcessBuilder to throw an exception
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Mockito.when(processBuilderMock.start()).thenThrow(new RuntimeException("Mocked Exception"));

        // Injecting the mock into the Cowsay class
        Cowsay cowsay = new Cowsay() {
            @Override
            protected ProcessBuilder createProcessBuilder() {
                return processBuilderMock;
            }
        };

        // Running the method and asserting the output
        String input = "Hello, World!";
        String actualOutput = cowsay.run(input);
        assertTrue("The output should be empty when an exception is thrown", actualOutput.isEmpty());
    }
}
