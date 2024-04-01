package com.memori.memori_domain.configurations;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@Profile("sqlite")
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean
    DataSource sqliteDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        final String driverClassName = env.getProperty("spring.datasource.driver-class-name");
        if (driverClassName == null)
            throw new IllegalArgumentException("Driver class name is not being setup");
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        return dataSource;
    }
}