package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserSignUpDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * This service contains everything related to the management of users.
 */
public interface UserService extends UserDetailsService {

    /**
     * Fetch all users for the specified page.
     *
     * @param pageable The page to get
     * @return Returns the users
     */
    List<ApplicationUser> getUsers(Pageable pageable);

    /**
     * Count all users in the table.
     *
     * @return Returns the number of users in the database
     */
    long countAll();

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not
     *                                   exist
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return an application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Fetch User by its id.
     *
     * @param id The id of the user to find.
     * @return Returns the Application User
     * @throws NotFoundException If given user(id) could not be found.
     */
    ApplicationUser getUserById(Long id) throws NotFoundException;

    /**
     * Log in a user.
     *
     * @param userLoginDto login credentials
     * @return the JWT, if successful
     * @throws org.springframework.security.authentication.BadCredentialsException if
     *                                                                             credentials
     *                                                                             are
     *                                                                             bad
     */
    String login(UserLoginDto userLoginDto);

    /**
     * Sign up a new user.
     *
     * @param userSignUpDto details of the user account, that should be created
     */
    void signUp(UserSignUpDto userSignUpDto) throws ValidationException;

    /**
     * Deletes the user.
     *
     * @param id       The id of the User
     * @param username The user who called this query
     * @throws NotFoundException       Thrown if the requested user could not be
     *                                 found
     * @throws UserPermissionException Thrown if the user is not the owner or an
     *                                 admin
     */
    void deleteUser(Long id, String username) throws NotFoundException, UserPermissionException;

    /**
     * Updates the user based on the specified information.
     *
     * @param userDto     The update user details
     * @param issuerEmail The email of the user that called this action
     * @return Returns the updated user
     * @throws NotFoundException       Thrown if the dto to update is not found
     * @throws UserPermissionException Thrown if the user that executed this query
     *                                 is not the user or an admin
     * @throws ValidationException     Thrown if the Details dto failed validation
     */
    ApplicationUser updateUser(@Valid UserUpdateDto userDto, String issuerEmail) throws NotFoundException,
        UserPermissionException,
        ValidationException;

    /**
     * creates the user based on the specified information.
     *
     * @param userDto     The update user details
     * @param issuerEmail The email of the user that called this action
     * @throws UserPermissionException Thrown if the user that executed this query
     *                                 is not the user or an admin
     * @throws ValidationException     Thrown if the Details dto failed validation
     */
    void createUser(@Valid UserCreateDto userDto, String issuerEmail) throws NotFoundException,
        UserPermissionException,
        ValidationException;

}
