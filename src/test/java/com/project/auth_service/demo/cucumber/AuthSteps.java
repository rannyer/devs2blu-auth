package com.project.auth_service.demo.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.auth_service.demo.models.dtos.LoginResponseDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.hu.Ha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AuthSteps.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> requestBody;
    private MvcResult response;
    private Map<String, Object> loginBody;
    @Autowired
    private TextContext textContext;

    @Given("a user exists with login {string} and password {string} and role {string}")
    public void aUserExistsWithLoginAndPassword(String login, String password, String role) throws Exception{
        textContext.getRegisterBody().put("login", login);
        textContext.getRegisterBody().put("password", password);
        textContext.getRegisterBody().put("role", role);

        mockMvc.perform(post("/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(textContext.getRegisterBody())))
                .andReturn();
    }

    @When("the user sends a POST request to {string} with the credentials")
    public void theUserSendsAPOSTRequestToWithTheCredentials(String endpoint) throws Exception {
        textContext.setResponse(mockMvc.perform(post(endpoint)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(textContext.getRegisterBody())))
                .andReturn());
    }
    @When("the user sends a POST request to {string} with the credentials {string} and password {string}")
    public void theUserSendsAPostRequestWIthLoginAndPassword(String endpoint, String login, String password) throws Exception {
        loginBody = new HashMap<>();
        loginBody.put("login", login);
        loginBody.put("password", password);

        textContext.setResponse(mockMvc.perform(post(endpoint)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andReturn());
    }

    @When("a user sends a POST request to {string} with login {string}, password {string}, and role {string}")
    public void registerWithInvalidRole(String endpoint, String login, String password, String role) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);
        body.put("role",role);

        textContext.setResponse(mockMvc.perform(post(endpoint)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn());

        System.out.println("REGISTER STATUS: " + textContext.getResponse().getResponse().getStatus());
        System.out.println("REGISTER BODY: "+textContext.getResponse().getResponse().getContentAsString());

    }




}
