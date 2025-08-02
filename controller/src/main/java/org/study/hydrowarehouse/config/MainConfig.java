package org.study.hydrowarehouse.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main configuration class for the Spring Boot application.
 * <p>
 * This class serves as the entry point for the application, bootstrapping the Spring context. It extends
 * {@link SpringBootServletInitializer} to support deployment as a traditional WAR in a servlet container.
 * The Hibernate JPA auto-configuration is excluded to allow for custom JPA configuration.
 * </p>
 *
 * @see SpringBootServletInitializer
 *
 * @author Aliaksandr Pishchala
 */
@SpringBootApplication(scanBasePackages = "org.study.hydrowarehouse",
        exclude = HibernateJpaAutoConfiguration.class)
public class MainConfig extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainConfig.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainConfig.class, args);
    }
}
