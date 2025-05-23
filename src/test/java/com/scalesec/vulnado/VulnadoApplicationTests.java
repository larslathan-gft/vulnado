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
		Postgres postgresMock = Mockito.mock(Postgres.class);
		SpringApplication springApplicationMock = Mockito.mock(SpringApplication.class);
		String[] args = {};

		// Act
		VulnadoApplication.main(args);

		// Assert
		Mockito.verify(postgresMock, Mockito.times(1)).setup();
		Mockito.verify(springApplicationMock, Mockito.times(1)).run(VulnadoApplication.class, args);
	}
}
