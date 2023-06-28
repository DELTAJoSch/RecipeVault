package at.ac.tuwien.sepm.groupphase.backend.validators;

import at.ac.tuwien.sepm.groupphase.backend.entity.Favorite;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class FavoriteValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * Validate an addition to favorites.
     *
     * @param favorite to be validated
     * @throws ValidationException if one of the ids is null
     */
    public static void validateForAddition(Favorite favorite) throws ValidationException {
        LOGGER.trace("validateForAddition({})", favorite);
        List<String> reasons = new ArrayList<>();

        if (favorite.getRecipe() == null) {
            reasons.add("recipe is null");
        }
        if (favorite.getUser() == null) {
            reasons.add("user is null");
        }
        if (!reasons.isEmpty()) {
            throw new ValidationException(reasons);
        }
    }
}
