package com.project.auth_service.demo.controller;

import com.project.auth_service.demo.repositories.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        repository.deleteAll();
    }

    @Test
    void deveAcessarRotaProtegidaComToken(){

        String registerPayload = """
                {
                    "login": "javeteri",
                    "password": "123",
                    "role": "VET"
                }
                """;
        RestAssured
                .given()
                    .body(registerPayload)
                    .contentType("application/json")
                .when()
                    .post("/auth/register")
                .then()
                    .statusCode(200);

        String payloadLogin = """
                   {
                    "login": "javeteri",
                    "password": "123"
                   }
                """;
        String token = RestAssured
                .given()
                    .contentType("application/json")
                    .body(payloadLogin)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
        System.out.println(token);

        RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                .when()
                    .get("/animais")
                .then()
                    .statusCode(200)
                .log().all();

    }

    @Test
    void deveRetornarErroQuandoSenhaEstiverIncorreta(){
        String registerPayload = """
                {
                    "login": "javeteri",
                    "password": "123",
                    "role": "VET"
                }
                """;
        RestAssured
                .given()
                .body(registerPayload)
                .contentType("application/json")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200);

        String payloadLogin = """
                   {
                    "login": "javeteri",
                    "password": "1234"
                   }
                """;

        RestAssured.given()
                        .contentType("application/json")
                        .body(payloadLogin)
                    .when()
                        .post("/auth/login")
                    .then()
                        .statusCode(403)
                    .log().all();
    }
    @Test
    void deveRetornarTokeNoLogin(){
        String registerPayload = """
                {
                    "login": "javeteri",
                    "password": "123",
                    "role": "VET"
                }
                """;
        RestAssured
                .given()
                .body(registerPayload)
                .contentType("application/json")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200);

        String payloadLogin = """
                   {
                    "login": "javeteri",
                    "password": "123"
                   }
                """;
        RestAssured
                .given()
                    .contentType("application/json")
                    .body(payloadLogin)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .body("token", notNullValue());
    }

    @Test
    void deveBloquearAcessoSemToken(){
        RestAssured
                .given()
                .when()
                    .get("/animais")
                .then()
                    .statusCode(403)
                    .log().all();
    }

    @Test
    void deveBloquearTokenInvalida(){
        RestAssured
                .given()
                   .header("Authorization", "Bearer tokenFake")
                .when()
                   .get("/animais")
                .then()
                    .statusCode(403)
                    .log().all();
        }



}
