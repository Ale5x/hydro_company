package org.study.hydro.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = "org.study.hydro",
        exclude = HibernateJpaAutoConfiguration.class)
@EnableTransactionManagement
@PropertySource("classpath:application-production.properties")
@Profile("production")
public class ProductionConfig {

    @Autowired
    private Environment environment;

    /**
     * LocalSessionFactoryBeann bean for Hibernate. It auto-configures dataSource.
     *
     * @return sessionFactory for Hibernate.
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(
                "org.study.hydro");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    /**
     * Data source. DataSource is part of the JDBC specification and can be seen as a generalized connection factory.
     * It allows a container or a framework to hide connection pooling and transaction management issues from the
     * application code.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        dataSource.setMaxActive(Integer.parseInt(environment.getProperty("spring.datasource.hikari.maximum-pool-size")));

        return dataSource;
    }

    /**
     * Platform Transaction Manager for Hibernate. This class provides simple access to a database through JDBC.
     *
     * @return the transactionManager.
     */
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    /**
     * Properties for Hibernate.
     *
     * @return hibernateProperties.
     */
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        return hibernateProperties;
    }
}
