package io.github.alberes.register.manager.config;

import io.github.alberes.register.manager.constants.MessageConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateConfig {

    @Bean
    public DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormatter.ofPattern(MessageConstants.DATE_TIME_FORMATTER_PATTERN);
    }
}
