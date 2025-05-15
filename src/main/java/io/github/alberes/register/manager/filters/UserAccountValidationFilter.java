package io.github.alberes.register.manager.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.alberes.register.manager.constants.MessageConstants;
import io.github.alberes.register.manager.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.register.manager.domains.UserPrincipal;
import io.github.alberes.register.manager.utils.JsonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    private static final String IGNORED_PATH = "/api/v1/users";

    private static final String IGNORED_AUTHENTICATED_PATH = "/api/v1/users/authenticated";

    private final JsonUtils jsonUtils = new JsonUtils(new ObjectMapper()
            .registerModule(new JavaTimeModule()));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
        if(!IGNORED_AUTHENTICATED_PATH.equals(request.getRequestURI()) && !IGNORED_PATH.equals(request.getRequestURI()) && !this.hasRoleAdmin(userPrincipal.getAuthorities())){
            String id = extractUserId(request.getRequestURI());
            if(!id.equals(userPrincipal.getUserAccount().getId().toString())){
                StandardErrorDto standardErrorDto =
                        new StandardErrorDto(System.currentTimeMillis(),
                                HttpStatus.UNAUTHORIZED.value(),
                                MessageConstants.UNAUTHORIZED, MessageConstants.UNAUTHORIZED_MESSAGE,
                                request.getRequestURI(),
                                List.of()
                        );
                response.setStatus(standardErrorDto.getStatus());
                response.setCharacterEncoding(MessageConstants.UTF_8);
                response.setContentType(MessageConstants.APPLICATION_JSON);
                response.getWriter().println(this.jsonUtils.toJson(standardErrorDto));
            }else{
                filterChain.doFilter(request, response);
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }

    private boolean hasRoleAdmin(Collection<? extends GrantedAuthority> roles){
        for(GrantedAuthority r : roles){
            if(r.getAuthority().equals(MessageConstants.ROLE_ADMIN)){
                return true;
            }
        }
        return false;
    }

    private String extractUserId(String path){
        return path.split(MessageConstants.SLASH)[4];
    }

}
