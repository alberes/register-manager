package io.github.alberes.register.manager.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.alberes.register.manager.controlles.exceptions.dto.StandardErrorDto;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.utils.JsonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserAccountValidationFilter extends OncePerRequestFilter {

    private final JsonUtils jsonUtils = new JsonUtils(new ObjectMapper()
            .registerModule(new JavaTimeModule()));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        String []paths = request.getRequestURI().split("/");
        String id = paths[paths.length - 1];
        if(!this.hasRoleAdmin(userPrincipal.getAuthorities())){
            if(!id.equals(userPrincipal.getId().toString())){
                StandardErrorDto standardErrorDto =
                        new StandardErrorDto(System.currentTimeMillis(),
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized", "The user can only access resources that belong to him.",
                                request.getRequestURI(),
                                List.of()
                        );
                response.setStatus(standardErrorDto.getStatus());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(this.jsonUtils.toJson(standardErrorDto));
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean hasRoleAdmin(Collection<? extends GrantedAuthority> roles){
        for(GrantedAuthority r : roles){
            if(r.getAuthority().equals("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }

}
