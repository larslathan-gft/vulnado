package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
		// Test to ensure the application context loads successfully
	}

	@Test
	public void main_ShouldInitializePostgresAndRunApplication() {
		// Mocking Postgres setup method
		Postgres mockPostgres = Mockito.mock(Postgres.class);
		doNothing().when(mockPostgres).setup();

		// Mocking SpringApplication.run
		SpringApplication mockSpringApplication = Mockito.mock(SpringApplication.class);
		doNothing().when(mockSpringApplication).run(VulnadoApplication.class, new String[]{});

		// Calling the main method
		VulnadoApplication.main(new String[]{});

		// Verifying that Postgres.setup() was called
		verify(mockPostgres, times(1)).setup();

		// Verifying that SpringApplication.run() was called
		verify(mockSpringApplication, times(1)).run(VulnadoApplication.class, new String[]{});
	}
}

