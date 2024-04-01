package com.memori.memori_data.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@Configuration
@EnableJpaRepositories(basePackages = "com.memori.memori_data.repositories.simple", 
                        repositoryBaseClass = SimpleJpaRepository.class)
public class SimpleJpaConfiguration {
}
