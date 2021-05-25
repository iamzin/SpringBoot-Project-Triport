package com.project.triport;

import com.project.triport.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TriportApplication {
    public static void main(String[] args) {
        SpringApplication.run(TriportApplication.class, args);
    }
}

