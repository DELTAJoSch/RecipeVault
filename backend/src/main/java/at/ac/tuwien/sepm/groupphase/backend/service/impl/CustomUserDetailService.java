package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserSignUpDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of the UserService.
 */
@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                   JwtTokenizer jwtTokenizer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.trace("loadUserByUsername({})", email);
        try {
            ApplicationUser applicationUser = findApplicationUserByEmail(email);

            List<GrantedAuthority> grantedAuthorities;
            if (applicationUser.getAdmin()) {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
            } else {
                grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            return new User(applicationUser.getEmail(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.trace("findApplicationUserByEmail({})", email);
        ApplicationUser applicationUser = userRepository.findByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Benutzer mit Emailadresse %s konnte nicht gefunden werden.", email));
    }

    @Override
    public ApplicationUser getUserById(Long id) throws NotFoundException {
        LOGGER.trace("getUserById({})", id);
        var applicationUser = userRepository.findById(id);
        if (applicationUser.isPresent()) {
            return applicationUser.get();
        }
        throw new NotFoundException("Benutzer mit id " + id + " konnte nicht gefunden werden.");
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        LOGGER.trace("login({})", userLoginDto);
        UserDetails userDetails = loadUserByUsername(userLoginDto.getEmail());
        if (userDetails != null
            && userDetails.isAccountNonExpired()
            && userDetails.isAccountNonLocked()
            && userDetails.isCredentialsNonExpired()
            && passwordEncoder.matches(userLoginDto.getPassword(),
            userDetails.getPassword())) {
            List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
            return jwtTokenizer.getAuthToken(userDetails.getUsername(), roles);
        }
        throw new BadCredentialsException("Benutzername oder Passwort  ist falsch.");
    }

    public void signUp(UserSignUpDto userSignUpDto) throws ValidationException {
        LOGGER.trace("Sign Up new User: CustomUserDetailService.signUp(email: " + userSignUpDto.getEmail() + ")");

        try {
            ApplicationUser userEntity = new ApplicationUser();
            userEntity.setEmail(userSignUpDto.getEmail());
            userEntity.setPassword(passwordEncoder.encode(userSignUpDto.getPassword()));

            this.userRepository.saveAndFlush(userEntity);
        } catch (DataIntegrityViolationException e) {
            List<String> l = new LinkedList<>();
            l.add("Ein anderer Benutzer mit dieser Email existiert schon.");
            throw new ValidationException(l, e);
        }
    }

    @Override
    public void deleteUser(Long id, String username) throws NotFoundException, UserPermissionException {
        var issuer = this.userRepository.findByEmail(username);
        if (id == null) {
            id = issuer.getId();
        }
        LOGGER.trace("delete({}, {})", id, username);
        if (issuer == null) {
            throw new InternalServerException("Benutzer konnte nicht in der Datenbank gefunden werden.");
        }
        if (!issuer.getAdmin() && !issuer.getId().equals(id)) {
            throw new UserPermissionException("Ausführender Benutzer ist kein Admin!");
        }

        Optional<ApplicationUser> dbEntry = this.userRepository.findById(id);
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Benutzer konnte nicht in der Datenbank gefunden werden.");
        }

        ApplicationUser entity = dbEntry.get();

        this.userRepository.delete(entity);
    }

    @Override
    public List<ApplicationUser> getUsers(Pageable pageable) {
        LOGGER.trace("getUsers({})", pageable);
        return this.userRepository.findAll(pageable).getContent();
    }

    @Override
    public long countAll() {
        return this.userRepository.count();
    }

    @Override
    public ApplicationUser updateUser(@Valid UserUpdateDto userDto, String issuerEmail)
        throws NotFoundException, UserPermissionException, ValidationException {
        LOGGER.trace("updateUser({}, {})", userDto, issuerEmail);
        var issuer = this.userRepository.findByEmail(issuerEmail);
        if (issuer == null) {
            throw new InternalServerException("Benutzer konnte nicht in der Datenbank gefunden werden.");
        }
        if (!issuer.getAdmin() && !issuer.getId().equals(userDto.getId()) && userDto.getId() != null) {
            throw new UserPermissionException("Ausführender Benutzer ist kein Admin!");
        }

        ApplicationUser entity;
        if (userDto.getId() != null) {
            var dbEntry = this.userRepository.findById(userDto.getId());
            if (dbEntry.isEmpty()) {
                throw new NotFoundException("Benutzer konnte nicht in der Datenbank gefunden werden.");
            }
            entity = dbEntry.get();
        } else {
            entity = userRepository.findByEmail(issuerEmail);
        }


        if (issuer.getAdmin()) { // if issuer is admin, update all including admin
            entity.setAdmin(userDto.isAdmin());
        } else if (userDto.isAdmin()) {
            throw new UserPermissionException("Du kannst dich nicht selbst zum Admin machen!");
        }

        // if the password is not set don't update it
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        try {
            entity.setEmail(userDto.getEmail());
            this.userRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            List<String> l = new LinkedList<>();
            l.add("Ein anderer Benutzer mit der selben Email existiert schon");
            throw new ValidationException(l, e);
        }

        return entity;
    }

    @Override
    public void createUser(@Valid UserCreateDto userDto, String issuerEmail)
        throws NotFoundException, UserPermissionException, ValidationException {
        LOGGER.trace("createUser({}, {})", userDto, issuerEmail);

        var issuer = this.userRepository.findByEmail(issuerEmail);
        if (issuer == null) {
            throw new InternalServerException("Benutzer konnte nicht in der Datenbank gefunden werden.");
        }
        if (!issuer.getAdmin()) {
            throw new UserPermissionException("Ausführender Benutzer ist kein Admin!");
        }

        try {
            ApplicationUser entity = new ApplicationUser();
            entity.setAdmin(userDto.isAdmin());
            entity.setEmail(userDto.getEmail());
            entity.setPassword(passwordEncoder.encode(userDto.getPassword()));

            this.userRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            List<String> l = new LinkedList<>();
            l.add("Ein anderer Benutzer mit der selben Email existiert schon");
            throw new ValidationException(l, e);
        }
    }
}
