package com.dekhokaun.mindarobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MindaroBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindaroBackendApplication.class, args);
    }

}
