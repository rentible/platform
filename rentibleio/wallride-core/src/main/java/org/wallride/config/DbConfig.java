package org.wallride.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Autowired
    private Environment env;

//    @Bean(name = "flatsharesDb")
//    @ConfigurationProperties(prefix = "flatshares.datasource")
//    public DataSource flatsharesDb() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("flatshares.datasource.driver-class-name"));
//        dataSource.setUrl(env.getProperty("flatshares.datasource.url"));
//        dataSource.setUsername(env.getProperty("flatshares.datasource.username"));
//        dataSource.setPassword(env.getProperty("flatshares.datasource.password"));
//        return dataSource;
//    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

//    @Bean(name = "flatSharesJdbcTemplate")
//    public JdbcTemplate jdbcTemplate(@Qualifier("flatsharesDb") DataSource flatsharesDb) {
//        return new JdbcTemplate(flatsharesDb);
//    }
//
//    @Bean(name = "flatSharesNamedParameterJdbcTemplate")
//    public NamedParameterJdbcTemplate oldNamedParameterJdbcTemplate(@Qualifier("flatsharesDb") DataSource flatsharesDb) {
//        return new NamedParameterJdbcTemplate(flatsharesDb);
//    }
}