package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * Endpoint to handle Request concerning the currently logged in user.
 */
@RestController
@RequestMapping(value = "/api/v1/user/me")
public class CurrentUserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final ApplicationUserMapper userMapper;

    @Autowired
    public CurrentUserEndpoint(UserService userService, ApplicationUserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Updates currently logged-in user, with data given in the update Dto.
     *
     * @param userUpdateDto  Data to update the user with.
     * @param authentication User triggering the Request.
     * @return UserInfoDto with the updated data.
     * @throws UserPermissionException Thrown if the user does not have the permission to do so.
     * @throws ValidationException     Thrown if the given data is invalid.
     */
    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "Update currently logged in user", security = @SecurityRequirement(name = "apiKey"))
    public UserInfoDto update(@Valid @RequestBody UserUpdateDto userUpdateDto, Authentication authentication) throws UserPermissionException, ValidationException {
        // set id to id of currently logged-in User
        LOGGER.info("POST /api/v1/user/me");
        userUpdateDto.setId(null);
        var result = userService.updateUser(userUpdateDto, authentication.getName());
        return userMapper.applicationUserToUserInfoDto(result);
    }

    /**
     * Retrieves current User from Database, based on the authentication Token.
     *
     * @param authentication User triggering the Request, User to get the detailed information for
     * @return UserDetails of user triggering the request.
     */
    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get detailed information on current user", security = @SecurityRequirement(name = "apiKey"))
    public UserInfoDto getUser(Authentication authentication) {
        LOGGER.info("GET /api/v1/user/me");
        return userMapper.applicationUserToUserInfoDto(userService.findApplicationUserByEmail(authentication.getName()));
    }

    /**
     * Deletes currently logged-in user from Database.
     *
     * @param authentication User triggering the Request, User to be deleted
     * @throws UserPermissionException Thrown if the user does not have permission to do so.
     */
    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "Delete currently logged-in user", security = @SecurityRequirement(name = "apiKey"))
    public void delete(Authentication authentication) throws UserPermissionException {
        LOGGER.info("DELETE /api/v1/user/me");
        userService.deleteUser(null, authentication.getName());
    }
}
