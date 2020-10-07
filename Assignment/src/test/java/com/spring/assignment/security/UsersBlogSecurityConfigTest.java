package com.spring.assignment.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class UsersBlogSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessProtected() throws Exception {
        this.mockMvc.perform(get("/blogs/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void loginUser() throws Exception {
        this.mockMvc.perform(get("/blogs/").with(httpBasic("user", "Welcome")))
                .andExpect(authenticated());
    }

    @Test
    public void loginInvalidUser() throws Exception {

        this.mockMvc.perform(get("/blogs").with(httpBasic("user1", "Welcome1")))
                .andExpect(status().isUnauthorized());

    }

}
