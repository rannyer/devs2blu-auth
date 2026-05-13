package com.project.auth_service.demo.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void deveAcessarRotaProtegidaComToken(){
        String payloadLogin = """
                   {
                    "login": "javet",
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




}
