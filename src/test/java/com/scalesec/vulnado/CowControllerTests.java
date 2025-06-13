import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CowController.class)
public class CowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Cowsay cowsay;

    @Test
    public void cowsay_ShouldReturnDefaultMessage() throws Exception {
        String defaultMessage = "I love Linux!";
        String expectedResponse = "Expected response for default message"; // Replace with actual expected response

        Mockito.when(cowsay.run(defaultMessage)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cowsay"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }

    @Test
    public void cowsay_ShouldReturnCustomMessage() throws Exception {
        String customMessage = "Hello, World!";
        String expectedResponse = "Expected response for custom message"; // Replace with actual expected response

        Mockito.when(cowsay.run(customMessage)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cowsay").param("input", customMessage))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
}
