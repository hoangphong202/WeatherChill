package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowRegistrationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

//    test tk dk thành công
    @Test
    public void testRegisterWithValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "newuser")
                        .param("email", "newuser@example.com")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));


    }


    //  test name trùng
    @Test
    public void testRegisterWithExistingName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "lgp")
                        .param("email", "newuser@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

    }

    //      test name rỗng
    @Test
    public void testRegisterWithNullName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "")
                        .param("email", "newuser@example.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

    }

    //  test email trùng
    @Test
    public void testRegisterWithExistingEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "newuser")
                        .param("email", "lgp@gmail.com")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

    }

    // test email rỗng
    @Test
    public void testRegisterWithNullEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "newuser")
                        .param("email", "")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

    }


    // test password rỗng
    @Test
    public void testRegisterWithNullPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("name", "newuser")
                        .param("email", "newuser@example.com")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }
}
