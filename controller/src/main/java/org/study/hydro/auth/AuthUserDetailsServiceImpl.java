package org.study.hydro.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * The class {@link AuthUserDetailsServiceImpl} provides the necessary information to construct
 * an Authentication object from the application's DAO objects
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public AuthUserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * The method of creating UserDetails in this case is through the username.
     *
     * @param username has the username.
     * @return UserDetails object.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.getUserByEmail(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.orElseThrow().getEmail(),
                user.orElseThrow().getPassword(),
                getAuthorities(user));
    }

    /**
     * The method will pack authorities and user roles into a special collection.
     *
     * @param user object with data.
     * @return SimpleGrantedAuthority collection.
     */
    private Collection<SimpleGrantedAuthority> getAuthorities(Optional<User> user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.orElseThrow().getRole().getName().name()));
        return authorities;
    }
}
