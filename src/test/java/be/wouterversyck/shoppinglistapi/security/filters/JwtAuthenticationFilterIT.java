package be.wouterversyck.shoppinglistapi.security.filters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class JwtAuthenticationFilterIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldGrantToken_WhenCorrectCredentialsAreGiven() throws Exception {
        mvc.perform(
                post("/login")
                        .content("{\"username\": \"user\",\"password\": \"password\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Token"));
    }

    @Test
    public void shouldDenyAccess_WhenInvalidCredentialsAreGiven() throws Exception {
        mvc.perform(
                post("/login")
                        .content("{\"username\": \"wrong\",\"password\": \"wrong\"}")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("X-Token"));
    }

    @Test
    public void shouldAllowAccess_WhenNotLoggedIn_ToPublicEndPoint() throws Exception {
        mvc.perform(get("/public/version"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("X-Token"));
    }

}
