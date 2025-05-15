package io.github.alberes.register.manager.services;

import io.github.alberes.register.manager.constants.MessageConstants;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface GenericService {

    default boolean hasRoleAdmin(Collection<? extends GrantedAuthority> roles){
        for(GrantedAuthority r : roles){
            if(r.getAuthority().equals(MessageConstants.ROLE_ADMIN)){
                return true;
            }
        }
        return false;
    }
}
