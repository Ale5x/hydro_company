package org.study.hydro.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.hydro.controller.ControllerConstants;
import org.study.hydro.controller.PathPages;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.utill.ImageStorage;


/**
 * The AuthenticationController type {@link AuthenticationController} is a controller that handles requests from clients: user
 * creation and user authentication
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    private final ImageStorage localImageStorage;

    @Value("${file.upload-user-dir}")
    private String uploadUserDir;

    @Autowired
    public AuthenticationController(AuthenticationService authService, ImageStorage localImageStorage) {
        this.authService = authService;
        this.localImageStorage = localImageStorage;
    }

    /**
     * The method creates a new user.
     *
     * @param userDto contains information about the new user.
     * @return HttpStatus with the authentication response.
     */
    @PostMapping(value = PathPages.AUTH_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> register(
            @RequestPart(ControllerConstants.DATE) UserDto userDto,
            @RequestParam(ControllerConstants.FILE) MultipartFile file
            ) {
        userDto.setPathPhoto(localImageStorage.save(file, uploadUserDir));
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
