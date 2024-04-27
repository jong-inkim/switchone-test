package com.switchwon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "switchwon 과제 api 명세서"))
public class SwitchwonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwitchwonApplication.class, args);
    }

}
