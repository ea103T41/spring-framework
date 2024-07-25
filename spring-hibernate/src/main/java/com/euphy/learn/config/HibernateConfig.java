package com.euphy.learn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("hibernate.properties")
public class HibernateConfig {

    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);
    private final Properties hibernateProperties = new Properties();

    public HibernateConfig() {
        try {
            hibernateProperties.load(
                    getClass().getClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            logger.error("Failed to load hibernate.properties", e);
        }
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.euphy.learn.model");
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(hibernateProperties.getProperty("hibernate.connection.driver_class"));
        dataSource.setUrl(hibernateProperties.getProperty("hibernate.connection.url"));
        dataSource.setUsername(hibernateProperties.getProperty("hibernate.connection.username"));
        dataSource.setPassword(hibernateProperties.getProperty("hibernate.connection.password"));
        return dataSource;
    }

}
