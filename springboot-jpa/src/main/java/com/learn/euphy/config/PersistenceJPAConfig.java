package com.learn.euphy.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.learn.euphy.model")
@EntityScan(basePackages = "com.learn.euphy.model")
@EnableJpaAuditing
public class PersistenceJPAConfig {
}
