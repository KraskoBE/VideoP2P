package com.krasen.web.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories( "com.krasen.web.repositories" )
@PropertySource( "classpath:application.properties" )
@EnableTransactionManagement
public class DbConfig {

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbDriver;

    @Autowired
    public DbConfig( Environment environment ) {
        dbUrl = environment.getProperty( "spring.datasource.url" );
        dbUsername = environment.getProperty( "spring.datasource.username" );
        dbPassword = environment.getProperty( "spring.datasource.password" );
        dbDriver = environment.getProperty( "spring.datasource.driver-class-name" );
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                                .username( dbUsername )
                                .password( dbPassword )
                                .url( dbUrl )
                                .driverClassName( dbDriver )
                                .build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl( true );

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter( vendorAdapter );
        factory.setPackagesToScan( "com.krasen.web.models" );
        factory.setDataSource( dataSource() );
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory( entityManagerFactory() );
        return txManager;
    }

}