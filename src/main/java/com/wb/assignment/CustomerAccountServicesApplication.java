package com.wb.assignment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer Account Services",
                version = "1.0.0",
                description = "REST APIs for managing customers accounts"
        ),
        tags = {
                @Tag(
                        name = "Accounts",
                        description = "Operations related to accounts"
                )
        }
)
@SpringBootApplication
public class CustomerAccountServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerAccountServicesApplication.class, args);
    }
}
