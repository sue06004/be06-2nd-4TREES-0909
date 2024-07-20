package org.example.fourtreesproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private Info info() {
        String description = "<h3>필독!</h3>  \n" +
                "<b>일반 유저 로그인 토큰</b>  \n" + "eyJhbGciOiJIUzI1NiJ9.eyJpZHgiOjMsImVtYWlsIjoic3VlMDYwMDRAbmF2ZXIuY29tIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyMTQ4MzkzMSwiZXhwIjoxODA3ODgzOTMxfQ.xXglA4iWsxVhI6_c3U1YvOReJ5xXI5pAtSdZHK--3KA  \n" +
                "<b>업체 유저 로그인 토큰</b>  \n" + "eyJhbGciOiJIUzI1NiJ9.eyJpZHgiOjYsImVtYWlsIjoic3VlMDYwMDRAZ21haWwuY29tIiwicm9sZSI6IlJPTEVfU0VMTEVSIiwiaWF0IjoxNzIxNDgzODkzLCJleHAiOjE4MDc4ODM4OTN9.eDtGLjur_ZisCqrGM7CyHGBb2oqKz3vuYoMrr3X9DW4  \n" +
                "로그인 url: /user/login  \n";
        return new Info().title("Gonggu").description(description).version("1.0.0");
    }

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .info(info())
                .addSecurityItem(securityRequirement)
                .components(components);
//        return new OpenAPI().components(new Components()).info(info());
    }

}
