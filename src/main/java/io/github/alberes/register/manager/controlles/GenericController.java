package io.github.alberes.register.manager.controlles;

import io.github.alberes.register.manager.domains.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

public interface GenericController {

    default boolean validate(String id){
        UserPrincipal userPrincipal = (UserPrincipal)SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Optional<? extends GrantedAuthority> roles = userPrincipal.getAuthorities().stream().filter(
                r -> "ADMIN".equals(r)
        ).findFirst();

        return !roles.isEmpty() || userPrincipal.getId().equals(id);
    }

    default URI createURI(String path, String id){
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(path)
                .buildAndExpand(id)
                .toUri();
    }
}