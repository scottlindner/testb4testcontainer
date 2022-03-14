package org.lindnersoft.helloapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfiguration {

    @Bean
    public String message(@Value("${message:Default Message}") String message) {
        return message;
    }
}
