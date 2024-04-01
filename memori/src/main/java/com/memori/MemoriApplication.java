package com.memori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.memori.memori_api",
    "com.memori.memori_data",
    "com.memori.memori_domain",
    "com.memori.memori_firebase",
    "com.memori.memori_security",
    "com.memori.memori_service",
})
public class MemoriApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MemoriApplication.class);
        app.run(args);
    }

}
