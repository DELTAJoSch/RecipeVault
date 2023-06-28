package at.ac.tuwien.sepm.groupphase.backend.engines;

import at.ac.tuwien.sepm.groupphase.backend.engines.containers.WineRecommendationResult;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;

import java.util.List;

/**
 * A WineRecommendationEngine produces Wine recommendations for Ingredients.
 */
public interface WineRecommendationEngine {
    /**
     * Generate a recommendation for the ingredients.
     *
     * @param ingredients The ingredients of the recipe and their amounts.
     * @return Returns a wine recommendation or null if no matching is possible / the confidence is very low
     */
    WineRecommendationResult generateRecommendation(List<Amount> ingredients);
}
