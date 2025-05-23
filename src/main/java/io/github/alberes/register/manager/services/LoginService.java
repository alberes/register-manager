package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.controllers.dto.LoginDto;
import io.github.alberes.register.manager.controllers.dto.TokenDto;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.services.exceptions.AuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationProvider provider;

    private final JWTService service;

    public TokenDto verify(LoginDto dto){

        Authentication authentication =
                this.provider.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        if(authentication.isAuthenticated()){
            UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
            return this.service.generateToken(userPrincipal);
        }else{
            throw new AuthorizationException(MessageConstants.AUTHORIZATION_FAILURE);
        }
    }
}
