package com.project.auth_service.demo.cucumber;

import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class TextContext {

    private Map<String, Object> registerBody = new HashMap<>();
    private Map<String, Object> loginBody = new HashMap<>();
    private String token;
    private MvcResult response;

    public Map<String, Object> getRegisterBody() {
        return registerBody;
    }

    public void setRegisterBody(Map<String, Object> registerBody) {
        this.registerBody = registerBody;
    }

    public Map<String, Object> getLoginBody() {
        return loginBody;
    }

    public void setLoginBody(Map<String, Object> loginBody) {
        this.loginBody = loginBody;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MvcResult getResponse() {
        return response;
    }

    public void setResponse(MvcResult response) {
        this.response = response;
    }
}
