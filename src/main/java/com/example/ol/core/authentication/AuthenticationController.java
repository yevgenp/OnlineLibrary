package com.example.ol.core.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class AuthenticationController {

    @RequestMapping("/api/authentication/login")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping("/api/authentication/logout")
    public ResponseEntity logout(HttpServletRequest request) throws ServletException {
        new SecurityContextLogoutHandler().logout(request, null, null);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
