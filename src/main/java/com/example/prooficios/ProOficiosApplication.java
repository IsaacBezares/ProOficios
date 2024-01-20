package com.example.prooficios;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "ProOficios API documentation",
                version = "1.0"
        )
)
public class ProOficiosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProOficiosApplication.class, args);
    }

}
