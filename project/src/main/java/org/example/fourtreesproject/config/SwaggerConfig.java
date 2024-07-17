package org.example.fourtreesproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private Info info (){
        return new Info().title("Gonggu").description("4tress 0909").version("1.0.0");
    }
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().components(new Components()).info(info());
    }

}
