package com.project.triport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TriportApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriportApplication.class, args);
    }

}

