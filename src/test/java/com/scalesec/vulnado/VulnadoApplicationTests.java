package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void main_ShouldSetupPostgresAndRunSpringApplication() {
		// Arrange
		String[] args = {};
		Mockito.mockStatic(Postgres.class);
		Mockito.mockStatic(SpringApplication.class);

		// Act
		VulnadoApplication.main(args);

		// Assert
		Mockito.verifyStatic(Postgres.class);
		Postgres.setup();
		Mockito.verifyStatic(SpringApplication.class);
		SpringApplication.run(VulnadoApplication.class, args);
	}
}
