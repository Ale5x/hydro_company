package org.study.hydro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Application-wide {@link ApplicationConfiguration} configuration class.
 *
 * @author Aliaksandr Pishchala
 */
@Configuration
public class ApplicationConfiguration {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationConfiguration(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Bean user details service, creates an object by username.
     * @return UserDetailsService Bean.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> mapUser(username);
    }

    private UserDetails mapUser(String username) {
        User user = userDao.getUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user));
    }

    /**
     * The AuthenticationProvider bean retrieves user details from a simple read-only user DAO, UserDetailsService.
     *
     * @return AuthenticationProvider Bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Processes an Authentication request.
     * @param config exports the authentication Configuration
     * @return AuthenticationManager Bean.
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * The method will pack authorities and user roles into a special collection.
     *
     * @param user object with data.
     * @return SimpleGrantedAuthority collection.
     */
    private Collection<SimpleGrantedAuthority> getAuthorities(User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName().name()));
        return authorities;
    }
}
