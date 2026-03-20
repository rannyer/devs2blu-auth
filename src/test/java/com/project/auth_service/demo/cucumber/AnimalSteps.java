package com.project.auth_service.demo.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.PendingException;
import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.glassfish.jaxb.runtime.v2.runtime.output.SAXOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest
public class AnimalSteps {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> registerBody;
    private MvcResult response;
    private String token;
    @Autowired
    private TextContext textContext;


    @Given("a user existst with login {string} and password {string} and role {string}")
    public void aUserExistsWithLoginAndPasswordAndRole(String login, String password, String role) throws Exception{
        registerBody = new HashMap<>();
        registerBody.put("login", login);
        registerBody.put("password", password);
        registerBody.put("role", role);

        MvcResult registerResponse =  mockMvc.perform(post("/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registerBody)))
                .andReturn();

        System.out.println("REGISTER STATUS: "+registerResponse.getResponse().getStatus());
        System.out.println("REGISTER BODY: "+registerResponse.getResponse().getContentAsString());
    }

    @Given("the user is athenticated at {string} with the credentials {string} and password {string}")
    public void theUserAuthenticatedAt(String endpoint, String login, String password) throws Exception{
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("login", login);
        loginBody.put("password", password);

        MvcResult loginResponse = mockMvc.perform(post(endpoint)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginBody)))
                .andReturn();

        System.out.println("LOGIN STATUS: "+loginResponse.getResponse().getStatus());
        System.out.println("LOGIN BODY: "+loginResponse.getResponse().getContentAsString());

        Map<String, Object> responseBody = objectMapper.readValue(loginResponse.getResponse().getContentAsString(), Map.class);
        token = (String) responseBody.get("token");
        System.out.println(token);
    }

    @When("the user sends a POST request to {string} with authenticated token and name {string}, species {string}")
    public void theUserSendsAPostRequestWithToken(String endpoint, String nome, String especie) throws Exception {
        Map<String, Object> animalBody = new HashMap<>();
        animalBody.put("nome", nome);
        animalBody.put("especie", especie);

        textContext.setResponse(mockMvc.perform(post(endpoint)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(animalBody)))
                .andReturn());

        System.out.println("ANIMAL REGISTER STATUS: "+textContext.getResponse().getResponse().getStatus());
        System.out.println("ANIMAL REGISTER BODY: "+textContext.getResponse().getResponse().getContentAsString());
    }





}
