package org.study.hydrowarehouse.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.*;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * The type Development configuration class is a development class which connects to the unreal database in the application.
 * The parameters of a connection is taken from the property file named "application-production.properties".
 *
 * @author Aliaksandr Pishchala
 */
@SpringBootApplication(scanBasePackages = "org.study.hydrowarehouse", exclude = HibernateJpaAutoConfiguration.class)
@PropertySource("classpath:application-development.properties")
@Profile("development")
@EnableTransactionManagement
public class DevelopmentConfig {

    private final DataSource dataSource;

    @Autowired
    public DevelopmentConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * LocalSessionFactoryBean bean for Hibernate. It auto-configures dataSource.
     *
     * @return sessionFactory for Hibernate.
     */
    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("org.study.hydrowarehouse");
        return sessionFactory;
    }

    /**
     * Platform Transaction Manager for Hibernate. This class provides simple access to a database through JDBC.
     *
     * @return the transactionManager.
     */
    @Bean
    public PlatformTransactionManager getPlatformTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }

    /**
     * Method for encrypting user password.
     * @return the PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
