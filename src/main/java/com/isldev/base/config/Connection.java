package com.isldev.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author YISivlay
 */
@Configuration
@EnableTransactionManagement
public class Connection {
    private final static Logger logger = LoggerFactory.getLogger(Connection.class);

    @Value("${PROTOCOL}")
    private String protocol;

    @Value("${SUB_PROTOCOL}")
    private String subProtocol;

    @Value("${DRIVER_CLASS_NAME}")
    private String driverClassName;

    @Value("${DATABASE}")
    private String databaseName;

    @Value("${DATABASE_USERNAME}")
    private String databaseUsername;

    @Value("${DATABASE_PASSWORD}")
    private String databasePassword;

    @Value("${ADDRESS}")
    private String address;

    @Value("${PORT}")
    private int port;

    @Bean
    public DataSource dataSource() {

        String url = protocol + ":" + subProtocol + "://" + address + ":" + port + "/" + databaseName;

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        try {
            dataSource.getConnection();
            logger.info("Successfully connected to database at {}", url);
        } catch (Exception e) {
            logger.error("Failed to connect to database at {}", url, e);
        }
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.isldev.base");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.current_session_context_class", "thread");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.c3p0.min_size", "5");
        jpaProperties.put("hibernate.c3p0.max_size", "20");
        jpaProperties.put("hibernate.c3p0.timeout", "300");
        jpaProperties.put("hibernate.c3p0.max_statements", "50");
        jpaProperties.put("hibernate.c3p0.idle_test_period", "3000");

        em.setJpaProperties(jpaProperties);
        return em;
    }
}