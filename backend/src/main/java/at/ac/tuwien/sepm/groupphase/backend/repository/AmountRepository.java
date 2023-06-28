package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for amounts.
 */
public interface AmountRepository extends JpaRepository<Amount, AmountKey> {
    long deleteByRecipe(Recipe recipe);
}
