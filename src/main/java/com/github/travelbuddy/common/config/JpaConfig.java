package com.github.travelbuddy.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.github.travelbuddy.board.repository",
                        "com.github.travelbuddy.chat.repository",
                        "com.github.travelbuddy.routes.repository",
                        "com.github.travelbuddy.users.repository",
                "com.github.travelbuddy.postImage.repository",
                "com.github.travelbuddy.likes.repository",
                "com.github.travelbuddy.trip.repository",
                "com.github.travelbuddy.comment.repository"
        },
        entityManagerFactoryRef = "entityManagerFactoryBean1"
)
@EnableConfigurationProperties(DataSourceProperties.class)
@RequiredArgsConstructor
public class JpaConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean1() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());

        em.setPackagesToScan("com.github.travelbuddy.board.entity",
                             "com.github.travelbuddy.chat.entity",
                             "com.github.travelbuddy.routes.entity",
                             "com.github.travelbuddy.users.entity",
                "com.github.travelbuddy.postImage.entity",
                "com.github.travelbuddy.likes.entity",
                "com.github.travelbuddy.trip.entity",
                "com.github.travelbuddy.comment.entity"
        );

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comment", "true");

        em.setJpaPropertyMap(properties);

        return em;
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        return dataSource;
    }
}
