package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.context.ApplicationContext;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private Postgres postgres;

    @Test
    public void contextLoads() {
        assertNotNull("Application context should be loaded", applicationContext);
    }

    @Test
    public void main_ShouldSetupPostgresAndRunSpringApplication() {
        // Arrange
        doNothing().when(postgres).setup();

        // Act
        VulnadoApplication.main(new String[]{});

        // Assert
        verify(postgres, times(1)).setup();
    }
}
