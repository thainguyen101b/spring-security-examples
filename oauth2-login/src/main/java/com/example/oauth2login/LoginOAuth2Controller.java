package com.example.oauth2login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginOAuth2Controller {

    @GetMapping("/login/oauth2")
    public String login() {
        return "login";
    }
}
