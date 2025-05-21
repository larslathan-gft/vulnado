import com.scalesec.vulnado.CowController;
import com.scalesec.vulnado.Cowsay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Cowsay cowsay;

    @Test
    public void cowsay_ShouldReturnDefaultMessage() throws Exception {
        String defaultMessage = "I love Linux!";
        String expectedResponse = "Mocked response for: " + defaultMessage;

        // Mocking the Cowsay.run method
        Mockito.when(cowsay.run(defaultMessage)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cowsay"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }

    @Test
    public void cowsay_ShouldReturnCustomMessage() throws Exception {
        String customMessage = "Hello, World!";
        String expectedResponse = "Mocked response for: " + customMessage;

        // Mocking the Cowsay.run method
        Mockito.when(cowsay.run(customMessage)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cowsay").param("input", customMessage))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
}
