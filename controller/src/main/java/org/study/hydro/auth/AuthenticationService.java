package org.study.hydro.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.security.JwtService;
import org.study.hydro.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The service class {@link AuthenticationService} is responsible for user authentication, provides opportunities for
 * registration and authentication.
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class AuthenticationService {

    private final UserDao userDao;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(UserDao userDao, UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userDao = userDao;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * The method will be responsible for registering the user.
     *
     * @param userDto is a class that has necessary data for registration.
     * @return AuthenticationResponse.
     */
    public AuthenticationResponse register(UserDto userDto) {
        if (userService.create(userDto)) {
            var jwtToken = jwtService.generateToken(getUserDetailService(userDto));
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .token("")
                    .build();
        }
    }

    /**
     * The method receives the necessary data from UserDto for UserDetails.
     *
     * @param userDto has the user's data.
     * @return UserDetails object.
     */
    private UserDetails getUserDetailService(UserDto userDto) {
        User user = new org.springframework.security.core.userdetails.User(
                userDto.getEmail(),
                userDto.getPassword(),
                getAuthorities(userDao.getUserByEmail(userDto.getEmail()).orElseThrow()));

        return user;
    }

    /**
     * The method is responsible for user authentication.
     *
     * @param request has the user's data for authentication.
     * @return AuthenticationResponse object.
     */
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        org.study.hydro.entity.User user = userDao.getUserByEmail(request.getEmail()).orElseThrow();
        User userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user));
        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    /**
     * The method will pack authorities and user roles into a special collection.
     *
     * @param user object with data.
     * @return SimpleGrantedAuthority collection.
     */
    private Collection<SimpleGrantedAuthority> getAuthorities(org.study.hydro.entity.User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName().name()));
        return authorities;
    }
}
