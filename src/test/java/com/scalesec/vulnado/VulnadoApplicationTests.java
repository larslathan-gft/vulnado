//BEGIN: /configuration/Work/DemoTestCreator/2025-09-23__14-16-32.183__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
		// Ensures the application context loads successfully
	}

	@Test
	public void main_ShouldInvokePostgresSetupAndRunApplication() {
		// Arrange
		Postgres mockPostgres = mock(Postgres.class);
		SpringApplication mockSpringApplication = mock(SpringApplication.class);

		// Act
		VulnadoApplication.main(new String[]{});

		// Assert
		verify(mockPostgres, times(1)).setup();
		verify(mockSpringApplication, times(1)).run(VulnadoApplication.class, new String[]{});
	}
}
//END: /configuration/Work/DemoTestCreator/2025-09-23__14-16-32.183__GenerateTests/Input/Existing_Tests/VulnadoApplicationTests.java
