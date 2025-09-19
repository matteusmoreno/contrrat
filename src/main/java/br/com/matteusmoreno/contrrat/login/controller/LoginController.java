package br.com.matteusmoreno.contrrat.login.controller;

import br.com.matteusmoreno.contrrat.login.request.LoginRequest;
import br.com.matteusmoreno.contrrat.login.response.LoginResponse;
import br.com.matteusmoreno.contrrat.login.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = loginService.login(request);

        return ResponseEntity.ok(response);
    }
}
