package io.github.alberes.register.manager.controllers;

import io.github.alberes.register.manager.controllers.dto.LoginDto;
import io.github.alberes.register.manager.controllers.dto.TokenDto;
import io.github.alberes.register.manager.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService service;

    @PostMapping
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto){

        TokenDto token = this.service.verify(dto);
        return ResponseEntity
                .status(HttpStatus.OK.value()).body(token);
    }

}