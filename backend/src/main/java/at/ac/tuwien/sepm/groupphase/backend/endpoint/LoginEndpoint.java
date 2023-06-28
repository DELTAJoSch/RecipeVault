package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint to handle login requests.
 */
@RestController
@RequestMapping(value = "/api/v1/authentication")
public class LoginEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public LoginEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handle login attempts.
     *
     * @param userLoginDto The data the user entered including email and password.
     * @return Returns a jwt as a string if successful.
     */
    @PermitAll
    @PostMapping
    public String login(@RequestBody UserLoginDto userLoginDto) {
        LOGGER.info("POST /api/v1/authentication/{}", userLoginDto.getEmail()); // Logs the request information
        return userService.login(userLoginDto);
    }
}
