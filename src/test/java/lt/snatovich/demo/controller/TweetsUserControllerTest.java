package lt.snatovich.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TweetsUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String CREATE_TWEET_CONTENT = "{\"text\": \"test\"}";

    private final String USERNAME = "0aecf584-7a32-4080-8625-da4e9e960498";

    @Test
    @WithAnonymousUser
    public void addTweetTestUnauthenticated() throws Exception {
        mockMvc.perform(post("/tweets/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_TWEET_CONTENT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(USERNAME)
    public void addTweetTest() throws Exception {
        mockMvc.perform(post("/tweets/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_TWEET_CONTENT))
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.text").value("test"),
                        jsonPath("$.publishedAt").exists(),
                        jsonPath("$.likes").value(0));
    }

    @Test
    @WithMockUser(USERNAME)
    public void getAllTweetsTestForbidden() throws Exception {
        mockMvc.perform(get("/tweets").with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = USERNAME, roles = "ADMIN")
    public void getAllTweetsTest() throws Exception {
        mockMvc.perform(get("/tweets").with(csrf()))
                .andExpectAll(status().isOk(),
                        jsonPath("$.content").exists(),
                        jsonPath("$.content").isArray());
    }
}
