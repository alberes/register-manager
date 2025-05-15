package io.github.alberes.register.manager.controllers;

import io.github.alberes.register.manager.domains.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public interface GenericController {

    default URI createURI(String path, String id){
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(path)
                .buildAndExpand(id)
                .toUri();
    }
}