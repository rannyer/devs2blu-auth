package com.project.auth_service.demo.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.auth_service.demo.models.dtos.LoginResponseDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
@AutoConfigureMockMvc
@SpringBootTest
public class AuthSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> requestBody;
    private MvcResult response;

    @Given("a user exists with login {string} and password {string} and role {string}")
    public void aUserExistsWithLoginAndPassword(String login, String password, String role) throws Exception{
        requestBody = new HashMap<>();
        requestBody.put("login", login);
        requestBody.put("password", password);
        requestBody.put("role", role);

        mockMvc.perform(post("/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestBody)))
                .andReturn();
    }

    @When("the user sends a POST request to {string} with the credentials")
    public void theUserSendsAPOSTRequestToWithTheCredentials(String endpoint) throws Exception {
        requestBody.remove("role");
        response = mockMvc.perform(post(endpoint)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(requestBody)))
            .andReturn();
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusShouldBe(Integer status) throws Exception {
        assertEquals(status.intValue(), response.getResponse().getStatus());

    }


}
