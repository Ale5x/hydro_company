package org.study.hydro.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.study.hydro.auth.AuthUserDetailsServiceImpl;
import org.study.hydro.controller.PathPages;
import org.study.hydro.entity.ERole;

/**
    * The class {@link SecurityConfiguration} provides the application security configuration.
    *
    * @author Aliaksandr Pishchala
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthUserDetailsServiceImpl authUserDetailsService;

    private static final String ERROR_FORBIDDEN_MESSAGE = "{\"error\": \"Forbidden\"}";
    private static final String ERROR_UNAUTHORIZED_MESSAGE = "{\"error\": \"Unauthorized\"}";


    @Autowired
    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthFilter,
            AuthenticationProvider authenticationProvider,
            AuthUserDetailsServiceImpl authUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.authUserDetailsService = authUserDetailsService;
    }

    /**
        * The method configures the application, separates the access level of each role.
        *
        * @param http is the HttpSecurity.
        *
        * @return http.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(PathPages.AUTH_ALL_RESOURCES, PathPages.GUEST_ALL_RESOURCES)
                .permitAll()
                .requestMatchers(PathPages.USER_ALL_RESOURCES)
                .hasAnyAuthority(
                        ERole.USER.name(),
                        ERole.ADMIN.name(),
                        ERole.MANAGER.name(),
                        ERole.CEO.name()
                )
                .requestMatchers(PathPages.API_ALL_RESOURCES)
                .hasAnyAuthority(
                        ERole.ADMIN.name(),
                        ERole.MANAGER.name(),
                        ERole.CEO.name()
                )
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(Customizer.withDefaults())
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(ERROR_UNAUTHORIZED_MESSAGE);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(ERROR_FORBIDDEN_MESSAGE);
                });
        return http.build();
    }
}