package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for users.
 */
@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    /**
     * Find specific user by its email.
     *
     * @param email the email of the user to get
     * @return the user with the specified email
     */
    ApplicationUser findByEmail(String email);

    /**
     * Find specific user by its id.
     *
     * @param id the id of the user to get
     * @return the user with the specified id
     */
    Optional<ApplicationUser> findById(Long id);

    /**
     * Find all users.
     *
     * @return a list of all users
     */
    List<ApplicationUser> findAll();

    /**
     * Find all users with matching attributes.
     *
     * @param pageable The requested page.
     * @return Returns a list of users matching the requested page
     */
    Page<ApplicationUser> findAll(Pageable pageable);

    /**
     * Deletes all users.
     */
    void deleteAll();
}
