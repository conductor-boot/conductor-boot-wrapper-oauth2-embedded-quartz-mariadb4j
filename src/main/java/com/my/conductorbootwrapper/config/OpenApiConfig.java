package com.my.conductorbootwrapper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
 
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Oauth2 & Quartz Scheduler APIs").description(
                        "This is Restful service for scheduling, pausing, resuming & deleting a Conductor Workflow Schedule"));
    }
}