package com.project.auth_service.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String tokenVet;

    @Test
    @Order(1)
    void deveRegistarrUsuarioVet() throws Exception{
        String body = """
        {
            "login": "vet2",
            "password": "vet123",
            "role": "VET"
        }
        """;

        mockMvc.perform(post("/auth/register")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isOk());
    }
    @Test
    @Order(2)
    void deveFazerLoginComSucessoERetornarToken() throws Exception{
        String body = """
            {
                "login": "vet2",
                "password": "vet123"
            }
            """;
        String response = mockMvc.perform(post("/auth/login")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        JsonNode json = objectMapper.readTree(response);
        tokenVet = json.get("token").asText();

        assertFalse(tokenVet == null || tokenVet.isEmpty());
    }

    @Test
    @Order(3)
    void naoDeveCriarAnimalSemToken() throws Exception{
        String body = """
            {
                "nome": "Rexadacimal",
                "especie": "C4ch0rr0"
            }
            """;

        mockMvc.perform(post("/animais")
            .contentType("application/json")
            .content(body))
            .andExpect(status().isForbidden());
    }
    @Test
    @Order(4)
    void deveCriarAnimalComTokenDeVet() throws Exception{
        String body = """
            {
                "nome": "Rexadacimal",
                "especie": "C4ch0rr0"
            }
            """;


        mockMvc.perform(post("/animais")
            .contentType("application/json")
            .header("Authorization", "Bearer " + tokenVet)
            .content(body))
            .andExpect(status().isOk());

    }

    //Feature
    //Scenario
    //Given
    //When
    //Then

    //gherkin -> linguagem de domínio específico para escrever testes de aceitação
    // Funcionalidade: Login do sistema
    // Cenário: Login com sucesso
    // Dado (considerando) que existe um usuario cadastro
    //Quando ele informar o login e senha validos
    //Entao o sistema de retornar com sucesso

    //Feature: System Login
    //Scenario: Successfull login
        //Given a registered user exsists
        //When the user provides valid login and password
        //Then the system should return a success response

    //Feature: login da api
        // Cenario: Login com credencias validas
            //dado que existe um usuario com login "admin" e senha "123445"
            //quando o usuario enviar uma requisicao de login com essas credencias
            //entao o sistema deve responder com status 200 e deve retoranr um token jwt

//    @Then("o ssitema deve res[mder com status 200")
//    public void validarStatus200(){
//        assertEquals(200, response.getStatus());
//    }






}
