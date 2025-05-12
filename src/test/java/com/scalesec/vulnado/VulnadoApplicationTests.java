package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.SpringApplication;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void main_ShouldSetupPostgresAndRunSpringApplication() {
        // Arrange
        Postgres mockPostgres = mock(Postgres.class);
        SpringApplication mockSpringApplication = mock(SpringApplication.class);
        String[] args = new String[]{};

        // Act
        VulnadoApplication.main(args);

        // Assert
        verify(mockPostgres, times(1)).setup();
        verify(mockSpringApplication, times(1)).run(VulnadoApplication.class, args);
    }
}
