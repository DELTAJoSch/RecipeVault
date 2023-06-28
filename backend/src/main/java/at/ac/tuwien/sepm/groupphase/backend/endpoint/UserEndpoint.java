package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Endpoint for User Requests.
 */
@RestController
@RequestMapping(value = "/api/v1/user") // base path for all endpoints
@Validated
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final ApplicationUserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, ApplicationUserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @return a page of users
     */
    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "get all users according to page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<UserInfoDto>> getUsers(
        @Valid
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size) {

        LOGGER.info("GET /api/v1/user");
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: {} Size: {}", page, size);

        var count = userService.countAll();
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(userMapper.applicationUserToUserInfoDto(userService.getUsers(pageable)));
    }

    /**
     * Updates User with given Data.
     *
     * @param userDto           Data to update the user with.
     * @param authentication    The user triggering the Request.
     * @return The updated UserDto
     * @throws UserPermissionException  Thrown if triggering user is not allowed to edit given user.
     * @throws ValidationException      Thrown if Validation of updateData fails.
     */
    @Secured("ROLE_USER")
    @PostMapping("/{id}")
    @Operation(summary = "Update user by id", security = @SecurityRequirement(name = "apiKey"))
    public UserInfoDto update(@Valid @RequestBody UserUpdateDto userDto, Authentication authentication)
        throws UserPermissionException,
        ValidationException {
        LOGGER.info("POST /api/v1/user/{}", userDto.getId()); // Logs the request information
        var result = userService.updateUser(userDto, authentication.getName());
        return userMapper.applicationUserToUserInfoDto(result);
    }

    /**
     * Create a new user.
     *
     * @param userDto        The new user's data
     * @param authentication The user triggering the request
     * @throws ValidationException Thrown if the data is invalid
     */
    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create a user", security = @SecurityRequirement(name = "apiKey"))
    public void create(@Valid @RequestBody UserCreateDto userDto, Authentication authentication)
        throws UserPermissionException,
        ValidationException {
        LOGGER.info("POST /api/v1/user");
        userService.createUser(userDto, authentication.getName());
    }

    /**
     * Delete the specified user.
     *
     * @param id             The id of the user to delete
     * @param authentication The user triggering the request
     * @throws UserPermissionException Thrown if the user does not have the correct
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the User specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id, Authentication authentication) throws UserPermissionException {
        LOGGER.info("DELETE /api/v1/user/{}", id);
        userService.deleteUser(id, authentication.getName());
    }

    /**
     * Get User by its id.
     *
     * @param id The id to find
     * @return Returns the user with specified id
     */
    @Secured("ROLE_USER")
    @GetMapping(params = {"id"})
    @Operation(summary = "Get User by its id", security = @SecurityRequirement(name = "apiKey"))
    public UserInfoDto getUserById(@RequestParam Long id) {
        LOGGER.info("GET api/v1/user?id={}", id);
        return userMapper.applicationUserToUserInfoDto(userService.getUserById(id));
    }

    /**
     * Get User by its email.
     *
     * @param email The email to find
     * @return Returns the user with specified email
     */
    @Secured("ROLE_USER")
    @GetMapping(params = {"email"})
    @Operation(summary = "Get User by its email", security = @SecurityRequirement(name = "apiKey"))
    public UserInfoDto getUserByEmail(@RequestParam String email) {
        LOGGER.info("GET api/v1/user?email={}", email);
        return userMapper.applicationUserToUserInfoDto(userService.findApplicationUserByEmail(email));
    }
}
