package com.project.auth_service.demo.cucumber;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public class CommonSteps {

    @Autowired
    private TextContext textContext;
    @Then("the response status code should be {int}")
    public void theResponseStatusShouldBe(Integer status) throws Exception {
        assertEquals(status.intValue(), textContext.getResponse().getResponse().getStatus());

    }
}
