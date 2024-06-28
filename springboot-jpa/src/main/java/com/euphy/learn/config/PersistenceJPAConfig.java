package com.euphy.learn.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.euphy.learn.model")
@EntityScan(basePackages = "com.euphy.learn.model")
@EnableJpaAuditing
public class PersistenceJPAConfig {
}
