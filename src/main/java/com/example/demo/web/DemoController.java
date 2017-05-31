package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DemoController {
    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @RequestMapping("/public")
    public ResponseEntity<String> open() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @RequestMapping("/protected")
    public ResponseEntity<String> authenticated(HttpServletRequest req) {
        return new ResponseEntity<>("Hello " + req.getRemoteUser() + ". Your IP address is " + req.getRemoteAddr(), HttpStatus.OK);
    }
}
