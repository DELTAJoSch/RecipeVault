package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserSignUpDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.invoke.MethodHandles;

/**
 * Endpoint for Signup Requests.
 */
@RestController
@RequestMapping(value = "/api/v1/signup")
public class SignUpEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public SignUpEndpoint(UserService userService) {
        this.userService = userService;
    }

    /**
     * Signs Up a new account. Creates Account with given Data.
     *
     * @param userSignUpDto Data to create account with.
     * @throws ValidationException  Thrown if validation of signup data fails.
     */
    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid UserSignUpDto userSignUpDto) throws ValidationException {
        LOGGER.info("POST /api/v1/signup");
        userService.signUp(userSignUpDto);
    }
}
