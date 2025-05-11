package io.github.alberes.register.manager.config;

import io.github.alberes.register.manager.filters.UserAccountValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserAccountValidationFilter> userAccountValidationFilter(){
        FilterRegistrationBean<UserAccountValidationFilter> userAccountValidationFilter =
                new FilterRegistrationBean<UserAccountValidationFilter>();
        userAccountValidationFilter.setFilter(new UserAccountValidationFilter());
        userAccountValidationFilter.addUrlPatterns("/api/v1/users/*", "/api/v1/users/{userId}/address/*");
        return userAccountValidationFilter;
    }
}
