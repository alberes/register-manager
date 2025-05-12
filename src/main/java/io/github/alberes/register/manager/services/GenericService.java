package io.github.alberes.register.manager.services;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface GenericService {

    default boolean hasRoleAdmin(Collection<? extends GrantedAuthority> roles){
        for(GrantedAuthority r : roles){
            if(r.getAuthority().equals("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }
}
