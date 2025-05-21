import com.scalesec.vulnado.VulnadoApplication;
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
        Postgres postgresMock = Mockito.mock(Postgres.class);
        SpringApplication springApplicationMock = Mockito.mock(SpringApplication.class);

        // Act
        VulnadoApplication.main(args);

        // Assert
        Mockito.verify(postgresMock).setup();
        Mockito.verify(springApplicationMock).run(VulnadoApplication.class, args);
    }
}
