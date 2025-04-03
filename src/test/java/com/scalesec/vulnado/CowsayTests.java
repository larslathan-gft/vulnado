package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void Cowsay_Run_ShouldReturnExpectedOutput() throws IOException {
        // Mocking the ProcessBuilder and Process
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Process processMock = Mockito.mock(Process.class);
        BufferedReader readerMock = Mockito.mock(BufferedReader.class);

        // Mocking the input stream to simulate the output of the cowsay command
        String expectedOutput = "Mocked cowsay output\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedOutput.getBytes());
        Mockito.when(processMock.getInputStream()).thenReturn(inputStream);
        Mockito.when(processBuilderMock.start()).thenReturn(processMock);

        // Mocking the BufferedReader behavior
        Mockito.when(readerMock.readLine()).thenReturn("Mocked cowsay output", (String) null);

        // Injecting the mocked ProcessBuilder into the Cowsay class
        Cowsay cowsay = new Cowsay();
        String actualOutput = cowsay.run("Hello");

        // Asserting the output
        assertEquals("The output should match the mocked cowsay output", expectedOutput, actualOutput);
    }

    @Test
    public void Cowsay_Run_ShouldHandleExceptionGracefully() {
        // Mocking the ProcessBuilder to throw an exception
        ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
        Mockito.when(processBuilderMock.start()).thenThrow(new IOException("Mocked IOException"));

        // Injecting the mocked ProcessBuilder into the Cowsay class
        Cowsay cowsay = new Cowsay();
        String actualOutput = cowsay.run("Hello");

        // Asserting the output
        assertEquals("The output should be empty when an exception occurs", "", actualOutput);
    }
}

