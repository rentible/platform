//package org.wallride.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import static java.util.Collections.singletonMap;
//
//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "secondEntityManagerFactory",
//        transactionManagerRef = "secondTransactionManager",
//        basePackages = "org.wallride.flatshares.repository"
//)
//public class FlatsharesDbConfig {
//    @Bean(name = "secondEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(final EntityManagerFactoryBuilder builder,
//                                                                             final @Qualifier("flatsharesDb") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("org.wallride.flatshares.domain")
//                .persistenceUnit("flatsharesDb")
//                .properties(singletonMap("hibernate.hbm2ddl.auto", "none"))
//                .build();
//    }
//
////    @Bean(name = "secondTransactionManager")
////    public PlatformTransactionManager secondTransactionManager(@Qualifier("secondEntityManagerFactory")
////                                                                       EntityManagerFactory secondEntityManagerFactory) {
////        return new JpaTransactionManager(secondEntityManagerFactory);
////    }
//}
