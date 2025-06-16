package com.example.customappuserdaoauthentication;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String index(Authentication auth) {
        return "hello: " + auth.getName();
    }

    @GetMapping("/admin")
    public String admin(Authentication auth) {
        return "hello admin: " + auth.getName();
    }

}