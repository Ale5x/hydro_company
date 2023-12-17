package org.study.hydro.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.*;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "org.study.hydro", exclude = HibernateJpaAutoConfiguration.class)
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
        sessionFactory.setPackagesToScan("org.study.hydro");
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
}
