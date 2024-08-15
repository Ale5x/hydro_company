package org.study.hydro.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.controller.PathPages;
import org.study.hydro.entity.Dto.UserDto;

/**
 * The AuthenticationController type {@link AuthenticationController} is a controller that handles requests from clients: user
 * creation and user authentication
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    @Autowired
    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * The method creates a new user.
     *
     * @param userDto contains information about the new user.
     * @return HttpStatus with the authentication response.
     */
    @PostMapping(PathPages.AUTH_CREATE)
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserDto userDto
            ) {
        return ResponseEntity.ok(authService.register(userDto));
    }

    /**
     * User authentication method.
     *
     * @param request contains information for user authentication in the system.
     *
     * @return HttpStatus with the authentication response.
     */
    @PostMapping(PathPages.AUTH_AUTHENTICATION)
    public ResponseEntity<AuthenticationResponse> authentication(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authentication(request));
    }
}
