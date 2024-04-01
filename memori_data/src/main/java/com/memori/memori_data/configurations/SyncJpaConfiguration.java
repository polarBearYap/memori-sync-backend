package com.memori.memori_data.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.memori.memori_data.repositories.sync.SyncEntityRepositoryImpl;

@Configuration
@EnableJpaRepositories(basePackages = "com.memori.memori_data.repositories.sync", 
                        repositoryBaseClass = SyncEntityRepositoryImpl.class)
public class SyncJpaConfiguration {
}
