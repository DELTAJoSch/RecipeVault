package at.ac.tuwien.sepm.groupphase.backend.engines.implementations;

import at.ac.tuwien.sepm.groupphase.backend.engines.WineRecommendationEngine;
import at.ac.tuwien.sepm.groupphase.backend.engines.containers.WineRecommendationResult;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default WineRecommendationEngine. It does not depend on external services.
 */
@Component
public class DefaultWineRecommendationEngine implements WineRecommendationEngine {
    private static final Map<IngredientMatchingCategory, Map<WineCategory, Double>> WEIGHTS;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Populate the Weight-Map
    static {
        WEIGHTS = new HashMap<>();

        var cheeseCrumblyMap = new HashMap<WineCategory, Double>();
        cheeseCrumblyMap.put(WineCategory.SPARKLING, 1.0);
        cheeseCrumblyMap.put(WineCategory.LIGHT_WHITE, 1.0);
        cheeseCrumblyMap.put(WineCategory.FULL_WHITE, 0.5);
        cheeseCrumblyMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        cheeseCrumblyMap.put(WineCategory.ROSE, 1.0);
        cheeseCrumblyMap.put(WineCategory.LIGHT_RED, 0.5);
        cheeseCrumblyMap.put(WineCategory.MIDDLE_RED, 0.0);
        cheeseCrumblyMap.put(WineCategory.FULL_RED, 0.0);
        cheeseCrumblyMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_CRUMBLY, cheeseCrumblyMap);

        var cheeseIntenseMap = new HashMap<WineCategory, Double>();
        cheeseIntenseMap.put(WineCategory.SPARKLING, 0.5);
        cheeseIntenseMap.put(WineCategory.LIGHT_WHITE, 0.0);
        cheeseIntenseMap.put(WineCategory.FULL_WHITE, 0.0);
        cheeseIntenseMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        cheeseIntenseMap.put(WineCategory.ROSE, 0.5);
        cheeseIntenseMap.put(WineCategory.LIGHT_RED, 0.0);
        cheeseIntenseMap.put(WineCategory.MIDDLE_RED, 0.0);
        cheeseIntenseMap.put(WineCategory.FULL_RED, 0.5);
        cheeseIntenseMap.put(WineCategory.DESSERT, 1.0);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_INTENSE, cheeseIntenseMap);

        var cheeseCreamMap = new HashMap<WineCategory, Double>();
        cheeseCreamMap.put(WineCategory.SPARKLING, 1.0);
        cheeseCreamMap.put(WineCategory.LIGHT_WHITE, 1.0);
        cheeseCreamMap.put(WineCategory.FULL_WHITE, 0.5);
        cheeseCreamMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        cheeseCreamMap.put(WineCategory.ROSE, 0.5);
        cheeseCreamMap.put(WineCategory.LIGHT_RED, 0.5);
        cheeseCreamMap.put(WineCategory.MIDDLE_RED, 0.5);
        cheeseCreamMap.put(WineCategory.FULL_RED, 0.0);
        cheeseCreamMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_CREAM, cheeseCreamMap);

        var cheeseDelicateMap = new HashMap<WineCategory, Double>();
        cheeseDelicateMap.put(WineCategory.SPARKLING, 1.0);
        cheeseDelicateMap.put(WineCategory.LIGHT_WHITE, 1.0);
        cheeseDelicateMap.put(WineCategory.FULL_WHITE, 1.0);
        cheeseDelicateMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        cheeseDelicateMap.put(WineCategory.ROSE, 0.5);
        cheeseDelicateMap.put(WineCategory.LIGHT_RED, 0.5);
        cheeseDelicateMap.put(WineCategory.MIDDLE_RED, 0.5);
        cheeseDelicateMap.put(WineCategory.FULL_RED, 0.5);
        cheeseDelicateMap.put(WineCategory.DESSERT, 1.0);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_DELICATE, cheeseDelicateMap);

        var cheeseNuttyMap = new HashMap<WineCategory, Double>();
        cheeseNuttyMap.put(WineCategory.SPARKLING, 1.0);
        cheeseNuttyMap.put(WineCategory.LIGHT_WHITE, 1.0);
        cheeseNuttyMap.put(WineCategory.FULL_WHITE, 1.0);
        cheeseNuttyMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        cheeseNuttyMap.put(WineCategory.ROSE, 0.5);
        cheeseNuttyMap.put(WineCategory.LIGHT_RED, 1.0);
        cheeseNuttyMap.put(WineCategory.MIDDLE_RED, 1.0);
        cheeseNuttyMap.put(WineCategory.FULL_RED, 0.5);
        cheeseNuttyMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_NUTTY, cheeseNuttyMap);

        var cheeseFruityMap = new HashMap<WineCategory, Double>();
        cheeseFruityMap.put(WineCategory.SPARKLING, 0.5);
        cheeseFruityMap.put(WineCategory.LIGHT_WHITE, 0.5);
        cheeseFruityMap.put(WineCategory.FULL_WHITE, 0.5);
        cheeseFruityMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        cheeseFruityMap.put(WineCategory.ROSE, 0.5);
        cheeseFruityMap.put(WineCategory.LIGHT_RED, 0.5);
        cheeseFruityMap.put(WineCategory.MIDDLE_RED, 1.0);
        cheeseFruityMap.put(WineCategory.FULL_RED, 1.0);
        cheeseFruityMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_FRUITY, cheeseFruityMap);

        var cheeseDrySaltyMap = new HashMap<WineCategory, Double>();
        cheeseDrySaltyMap.put(WineCategory.SPARKLING, 1.0);
        cheeseDrySaltyMap.put(WineCategory.LIGHT_WHITE, 1.0);
        cheeseDrySaltyMap.put(WineCategory.FULL_WHITE, 0.5);
        cheeseDrySaltyMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        cheeseDrySaltyMap.put(WineCategory.ROSE, 0.5);
        cheeseDrySaltyMap.put(WineCategory.LIGHT_RED, 0.5);
        cheeseDrySaltyMap.put(WineCategory.MIDDLE_RED, 1.0);
        cheeseDrySaltyMap.put(WineCategory.FULL_RED, 0.5);
        cheeseDrySaltyMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.CHEESE_DRY_SALTY, cheeseDrySaltyMap);

        var crustaceansMap = new HashMap<WineCategory, Double>();
        crustaceansMap.put(WineCategory.SPARKLING, 1.0);
        crustaceansMap.put(WineCategory.LIGHT_WHITE, 1.0);
        crustaceansMap.put(WineCategory.FULL_WHITE, 1.0);
        crustaceansMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        crustaceansMap.put(WineCategory.ROSE, 0.5);
        crustaceansMap.put(WineCategory.LIGHT_RED, 0.5);
        crustaceansMap.put(WineCategory.MIDDLE_RED, 0.0);
        crustaceansMap.put(WineCategory.FULL_RED, 0.0);
        crustaceansMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.CRUSTACEANS, crustaceansMap);

        var shellfishMap = new HashMap<WineCategory, Double>();
        shellfishMap.put(WineCategory.SPARKLING, 1.0);
        shellfishMap.put(WineCategory.LIGHT_WHITE, 0.5);
        shellfishMap.put(WineCategory.FULL_WHITE, 0.5);
        shellfishMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        shellfishMap.put(WineCategory.ROSE, 0.0);
        shellfishMap.put(WineCategory.LIGHT_RED, 0.5);
        shellfishMap.put(WineCategory.MIDDLE_RED, 0.0);
        shellfishMap.put(WineCategory.FULL_RED, 0.0);
        shellfishMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.SHELLFISH, shellfishMap);

        var fishMap = new HashMap<WineCategory, Double>();
        fishMap.put(WineCategory.SPARKLING, 0.5);
        fishMap.put(WineCategory.LIGHT_WHITE, 1.0);
        fishMap.put(WineCategory.FULL_WHITE, 0.5);
        fishMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        fishMap.put(WineCategory.ROSE, 0.5);
        fishMap.put(WineCategory.LIGHT_RED, 0.5);
        fishMap.put(WineCategory.MIDDLE_RED, 0.0);
        fishMap.put(WineCategory.FULL_RED, 0.0);
        fishMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.FISH, fishMap);

        var whiteMeatMap = new HashMap<WineCategory, Double>();
        whiteMeatMap.put(WineCategory.SPARKLING, 0.5);
        whiteMeatMap.put(WineCategory.LIGHT_WHITE, 0.5);
        whiteMeatMap.put(WineCategory.FULL_WHITE, 1.0);
        whiteMeatMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        whiteMeatMap.put(WineCategory.ROSE, 1.0);
        whiteMeatMap.put(WineCategory.LIGHT_RED, 1.0);
        whiteMeatMap.put(WineCategory.MIDDLE_RED, 1.0);
        whiteMeatMap.put(WineCategory.FULL_RED, 0.5);
        whiteMeatMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.MEAT_WHITE, whiteMeatMap);

        var redMeatMap = new HashMap<WineCategory, Double>();
        redMeatMap.put(WineCategory.SPARKLING, 0.0);
        redMeatMap.put(WineCategory.LIGHT_WHITE, 0.0);
        redMeatMap.put(WineCategory.FULL_WHITE, 0.0);
        redMeatMap.put(WineCategory.AROMATIC_WHITE, 0.0);
        redMeatMap.put(WineCategory.ROSE, 0.0);
        redMeatMap.put(WineCategory.LIGHT_RED, 0.5);
        redMeatMap.put(WineCategory.MIDDLE_RED, 1.0);
        redMeatMap.put(WineCategory.FULL_RED, 1.0);
        redMeatMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.MEAT_RED, redMeatMap);

        var curedMeatMap = new HashMap<WineCategory, Double>();
        curedMeatMap.put(WineCategory.SPARKLING, 1.0);
        curedMeatMap.put(WineCategory.LIGHT_WHITE, 0.5);
        curedMeatMap.put(WineCategory.FULL_WHITE, 0.0);
        curedMeatMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        curedMeatMap.put(WineCategory.ROSE, 1.0);
        curedMeatMap.put(WineCategory.LIGHT_RED, 0.5);
        curedMeatMap.put(WineCategory.MIDDLE_RED, 0.5);
        curedMeatMap.put(WineCategory.FULL_RED, 0.5);
        curedMeatMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.MEAT_CURED, curedMeatMap);

        var marinadeIntenseMap = new HashMap<WineCategory, Double>();
        marinadeIntenseMap.put(WineCategory.SPARKLING, 0.5);
        marinadeIntenseMap.put(WineCategory.LIGHT_WHITE, 0.0);
        marinadeIntenseMap.put(WineCategory.FULL_WHITE, 0.0);
        marinadeIntenseMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        marinadeIntenseMap.put(WineCategory.ROSE, 0.5);
        marinadeIntenseMap.put(WineCategory.LIGHT_RED, 0.0);
        marinadeIntenseMap.put(WineCategory.MIDDLE_RED, 1.0);
        marinadeIntenseMap.put(WineCategory.FULL_RED, 0.5);
        marinadeIntenseMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.MARINADE_INTENSE, marinadeIntenseMap);

        var vegetableCruciferousMap = new HashMap<WineCategory, Double>();
        vegetableCruciferousMap.put(WineCategory.SPARKLING, 0.5);
        vegetableCruciferousMap.put(WineCategory.LIGHT_WHITE, 0.5);
        vegetableCruciferousMap.put(WineCategory.FULL_WHITE, 0.0);
        vegetableCruciferousMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        vegetableCruciferousMap.put(WineCategory.ROSE, 0.5);
        vegetableCruciferousMap.put(WineCategory.LIGHT_RED, 0.5);
        vegetableCruciferousMap.put(WineCategory.MIDDLE_RED, 0.0);
        vegetableCruciferousMap.put(WineCategory.FULL_RED, 0.0);
        vegetableCruciferousMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_CRUCIFEROUS, vegetableCruciferousMap);

        var vegetableGreenMap = new HashMap<WineCategory, Double>();
        vegetableGreenMap.put(WineCategory.SPARKLING, 0.5);
        vegetableGreenMap.put(WineCategory.LIGHT_WHITE, 1.0);
        vegetableGreenMap.put(WineCategory.FULL_WHITE, 0.5);
        vegetableGreenMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        vegetableGreenMap.put(WineCategory.ROSE, 0.5);
        vegetableGreenMap.put(WineCategory.LIGHT_RED, 0.5);
        vegetableGreenMap.put(WineCategory.MIDDLE_RED, 0.0);
        vegetableGreenMap.put(WineCategory.FULL_RED, 0.0);
        vegetableGreenMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_GREEN, vegetableGreenMap);

        var vegetableRootMap = new HashMap<WineCategory, Double>();
        vegetableRootMap.put(WineCategory.SPARKLING, 1.0);
        vegetableRootMap.put(WineCategory.LIGHT_WHITE, 1.0);
        vegetableRootMap.put(WineCategory.FULL_WHITE, 0.5);
        vegetableRootMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        vegetableRootMap.put(WineCategory.ROSE, 1.0);
        vegetableRootMap.put(WineCategory.LIGHT_RED, 0.5);
        vegetableRootMap.put(WineCategory.MIDDLE_RED, 0.5);
        vegetableRootMap.put(WineCategory.FULL_RED, 0.0);
        vegetableRootMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_ROOTS, vegetableRootMap);

        var vegetableBulbousMap = new HashMap<WineCategory, Double>();
        vegetableBulbousMap.put(WineCategory.SPARKLING, 0.5);
        vegetableBulbousMap.put(WineCategory.LIGHT_WHITE, 1.0);
        vegetableBulbousMap.put(WineCategory.FULL_WHITE, 1.0);
        vegetableBulbousMap.put(WineCategory.AROMATIC_WHITE, 0.5);
        vegetableBulbousMap.put(WineCategory.ROSE, 0.5);
        vegetableBulbousMap.put(WineCategory.LIGHT_RED, 1.0);
        vegetableBulbousMap.put(WineCategory.MIDDLE_RED, 1.0);
        vegetableBulbousMap.put(WineCategory.FULL_RED, 0.5);
        vegetableBulbousMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_BULBOUS, vegetableBulbousMap);

        var vegetableNightShadeMap = new HashMap<WineCategory, Double>();
        vegetableNightShadeMap.put(WineCategory.SPARKLING, 0.0);
        vegetableNightShadeMap.put(WineCategory.LIGHT_WHITE, 0.5);
        vegetableNightShadeMap.put(WineCategory.FULL_WHITE, 0.0);
        vegetableNightShadeMap.put(WineCategory.AROMATIC_WHITE, 0.0);
        vegetableNightShadeMap.put(WineCategory.ROSE, 0.5);
        vegetableNightShadeMap.put(WineCategory.LIGHT_RED, 0.5);
        vegetableNightShadeMap.put(WineCategory.MIDDLE_RED, 1.0);
        vegetableNightShadeMap.put(WineCategory.FULL_RED, 0.5);
        vegetableNightShadeMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW, vegetableNightShadeMap);

        var vegetableLegumesMap = new HashMap<WineCategory, Double>();
        vegetableLegumesMap.put(WineCategory.SPARKLING, 0.5);
        vegetableLegumesMap.put(WineCategory.LIGHT_WHITE, 0.5);
        vegetableLegumesMap.put(WineCategory.FULL_WHITE, 0.0);
        vegetableLegumesMap.put(WineCategory.AROMATIC_WHITE, 0.0);
        vegetableLegumesMap.put(WineCategory.ROSE, 0.5);
        vegetableLegumesMap.put(WineCategory.LIGHT_RED, 0.5);
        vegetableLegumesMap.put(WineCategory.MIDDLE_RED, 1.0);
        vegetableLegumesMap.put(WineCategory.FULL_RED, 1.0);
        vegetableLegumesMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_LEGUMES, vegetableLegumesMap);

        var vegetableMushroomsMap = new HashMap<WineCategory, Double>();
        vegetableMushroomsMap.put(WineCategory.SPARKLING, 0.5);
        vegetableMushroomsMap.put(WineCategory.LIGHT_WHITE, 0.0);
        vegetableMushroomsMap.put(WineCategory.FULL_WHITE, 1.0);
        vegetableMushroomsMap.put(WineCategory.AROMATIC_WHITE, 0.0);
        vegetableMushroomsMap.put(WineCategory.ROSE, 0.5);
        vegetableMushroomsMap.put(WineCategory.LIGHT_RED, 1.0);
        vegetableMushroomsMap.put(WineCategory.MIDDLE_RED, 1.0);
        vegetableMushroomsMap.put(WineCategory.FULL_RED, 0.5);
        vegetableMushroomsMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.VEGETABLE_MUSHROOMS, vegetableMushroomsMap);

        var herbsAromaticMap = new HashMap<WineCategory, Double>();
        herbsAromaticMap.put(WineCategory.SPARKLING, 0.5);
        herbsAromaticMap.put(WineCategory.LIGHT_WHITE, 1.0);
        herbsAromaticMap.put(WineCategory.FULL_WHITE, 0.0);
        herbsAromaticMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        herbsAromaticMap.put(WineCategory.ROSE, 1.0);
        herbsAromaticMap.put(WineCategory.LIGHT_RED, 0.5);
        herbsAromaticMap.put(WineCategory.MIDDLE_RED, 0.5);
        herbsAromaticMap.put(WineCategory.FULL_RED, 0.5);
        herbsAromaticMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.HERBS_AROMATIC, herbsAromaticMap);

        var herbsDryMap = new HashMap<WineCategory, Double>();
        herbsDryMap.put(WineCategory.SPARKLING, 0.5);
        herbsDryMap.put(WineCategory.LIGHT_WHITE, 1.0);
        herbsDryMap.put(WineCategory.FULL_WHITE, 0.5);
        herbsDryMap.put(WineCategory.AROMATIC_WHITE, 0.0);
        herbsDryMap.put(WineCategory.ROSE, 0.5);
        herbsDryMap.put(WineCategory.LIGHT_RED, 0.5);
        herbsDryMap.put(WineCategory.MIDDLE_RED, 1.0);
        herbsDryMap.put(WineCategory.FULL_RED, 0.5);
        herbsDryMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.HERBS_DRY, herbsDryMap);

        var herbsResinousMap = new HashMap<WineCategory, Double>();
        herbsResinousMap.put(WineCategory.SPARKLING, 0.5);
        herbsResinousMap.put(WineCategory.LIGHT_WHITE, 0.5);
        herbsResinousMap.put(WineCategory.FULL_WHITE, 0.0);
        herbsResinousMap.put(WineCategory.AROMATIC_WHITE, 0.0);
        herbsResinousMap.put(WineCategory.ROSE, 0.5);
        herbsResinousMap.put(WineCategory.LIGHT_RED, 0.5);
        herbsResinousMap.put(WineCategory.MIDDLE_RED, 0.5);
        herbsResinousMap.put(WineCategory.FULL_RED, 0.5);
        herbsResinousMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.HERBS_RESINOUS, herbsResinousMap);

        var herbsExoticMap = new HashMap<WineCategory, Double>();
        herbsExoticMap.put(WineCategory.SPARKLING, 0.5);
        herbsExoticMap.put(WineCategory.LIGHT_WHITE, 0.0);
        herbsExoticMap.put(WineCategory.FULL_WHITE, 0.0);
        herbsExoticMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        herbsExoticMap.put(WineCategory.ROSE, 0.0);
        herbsExoticMap.put(WineCategory.LIGHT_RED, 0.5);
        herbsExoticMap.put(WineCategory.MIDDLE_RED, 1.0);
        herbsExoticMap.put(WineCategory.FULL_RED, 0.0);
        herbsExoticMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.HERBS_EXOTIC, herbsExoticMap);

        var herbsBakingMap = new HashMap<WineCategory, Double>();
        herbsBakingMap.put(WineCategory.SPARKLING, 0.5);
        herbsBakingMap.put(WineCategory.LIGHT_WHITE, 0.0);
        herbsBakingMap.put(WineCategory.FULL_WHITE, 0.5);
        herbsBakingMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        herbsBakingMap.put(WineCategory.ROSE, 0.5);
        herbsBakingMap.put(WineCategory.LIGHT_RED, 0.5);
        herbsBakingMap.put(WineCategory.MIDDLE_RED, 1.0);
        herbsBakingMap.put(WineCategory.FULL_RED, 0.5);
        herbsBakingMap.put(WineCategory.DESSERT, 0.5);
        WEIGHTS.put(IngredientMatchingCategory.HERBS_BAKING, herbsBakingMap);

        var herbsUmamiMap = new HashMap<WineCategory, Double>();
        herbsUmamiMap.put(WineCategory.SPARKLING, 0.0);
        herbsUmamiMap.put(WineCategory.LIGHT_WHITE, 0.5);
        herbsUmamiMap.put(WineCategory.FULL_WHITE, 0.5);
        herbsUmamiMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        herbsUmamiMap.put(WineCategory.ROSE, 0.5);
        herbsUmamiMap.put(WineCategory.LIGHT_RED, 0.5);
        herbsUmamiMap.put(WineCategory.MIDDLE_RED, 1.0);
        herbsUmamiMap.put(WineCategory.FULL_RED, 1.0);
        herbsUmamiMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.HERBS_UMAMI, herbsUmamiMap);

        var peppersChillisMap = new HashMap<WineCategory, Double>();
        peppersChillisMap.put(WineCategory.SPARKLING, 0.0);
        peppersChillisMap.put(WineCategory.LIGHT_WHITE, 0.5);
        peppersChillisMap.put(WineCategory.FULL_WHITE, 0.0);
        peppersChillisMap.put(WineCategory.AROMATIC_WHITE, 1.0);
        peppersChillisMap.put(WineCategory.ROSE, 0.5);
        peppersChillisMap.put(WineCategory.LIGHT_RED, 0.0);
        peppersChillisMap.put(WineCategory.MIDDLE_RED, 0.5);
        peppersChillisMap.put(WineCategory.FULL_RED, 0.0);
        peppersChillisMap.put(WineCategory.DESSERT, 0.0);
        WEIGHTS.put(IngredientMatchingCategory.PEPPERS_CHILLIS, peppersChillisMap);
    }

    @Override
    public WineRecommendationResult generateRecommendation(List<Amount> ingredients) {
        LOGGER.trace("generateRecommendation({})", ingredients);
        if (ingredients == null || ingredients.isEmpty()) {
            return null;
        }

        var converted = this.convertUnits(ingredients);

        if (converted.isEmpty()) {
            return null;
        }

        var percentages = this.mapToPercentages(converted);
        var matchCount = this.getMatchCount(percentages);

        var category = this.getMatch(matchCount);
        double confidence = this.getMatchConfidence(matchCount, category);

        // not confident in match
        if (confidence < 0.1) {
            return null;
        }

        return new WineRecommendationResult.WineRecommendationResultBuilder()
            .confidence(confidence)
            .category(category)
            .build();
    }

    /**
     * Calculate the confidence in the match.
     *
     * @param matchCount The matching numbers for the categories.
     * @param match      The matching category
     * @return Returns the confidence in the match.
     */
    private double getMatchConfidence(Map<WineCategory, Double> matchCount, WineCategory match) {
        LOGGER.trace("getMatchConfidence({}, {})", matchCount, match);
        var total = matchCount.values().stream().mapToDouble(Double::doubleValue).sum();

        return matchCount.get(match) / total;
    }

    /**
     * Extract the winning wine category from the matchCount.
     *
     * @param matchCount The matching number for the categories.
     * @return Returns the matching category.
     */
    private WineCategory getMatch(Map<WineCategory, Double> matchCount) {
        LOGGER.trace("getMatch({})", matchCount);
        WineCategory match = WineCategory.DESSERT;
        var running = 0.0;

        for (var entry : matchCount.entrySet()) {
            if (entry.getValue() > running) {
                running = entry.getValue();
                match = entry.getKey();
            }
        }

        return match;
    }

    /**
     * Calculate absolute values for each wine category.
     *
     * @param percentages The percentages for each IngredientCategory
     * @return Returns an absolute count of the matches.
     */
    private Map<WineCategory, Double> getMatchCount(Map<IngredientMatchingCategory, Double> percentages) {
        LOGGER.trace("getMatchCount({})", percentages);
        var matchCount = new HashMap<WineCategory, Double>();

        for (var entry : percentages.entrySet()) {
            var key = entry.getKey();

            if (key == null) {
                continue;
            }

            for (var weightPair : WEIGHTS.get(key).entrySet()) {
                var old = matchCount.getOrDefault(weightPair.getKey(), 0.0);

                old += entry.getValue() * weightPair.getValue();

                matchCount.put(weightPair.getKey(), old);
            }
        }

        return matchCount;
    }

    /**
     * Calculate the weight per category in grams.
     *
     * @param ingredients The ingredients to map
     * @return Returns the amount in grams for each ingredient category
     */
    private Map<IngredientMatchingCategory, Double> convertUnits(List<Amount> ingredients) {
        LOGGER.trace("convertUnits({})", ingredients);
        var converted = new HashMap<IngredientMatchingCategory, Double>();

        for (var ingredient : ingredients) {
            var ing = ingredient.getIngredient();
            var category = ing.getCategory();

            if (category == IngredientMatchingCategory.UNDEFINED) {
                continue;
            }

            double categoryAmount = converted.getOrDefault(category, 0.0);

            switch (ingredient.getUnit()) {
                case G, ML -> {
                    categoryAmount += ingredient.getAmount(); // 1g = 1g (for ml: 1g of water)
                }
                case PIECE -> {
                    categoryAmount += ingredient.getAmount() * 100; // 1 piece is approximated with 100g - this is an average assumed by ChatGPT :)
                }
                case LB -> {
                    categoryAmount += ingredient.getAmount() * 453.592; // 1lb = 453,592g
                }
                case TSP -> {
                    categoryAmount += ingredient.getAmount() * 5.69; // 1tsp = 5.69g
                }
                case TBSP -> {
                    categoryAmount += ingredient.getAmount() * 17.07; // 1tbsp = 17.07g
                }
                case KG -> {
                    categoryAmount += ingredient.getAmount() * 1000; // 1kg = 1000g
                }
                case Fl -> {
                    categoryAmount += ingredient.getAmount() * 29.58; // 1fl oz is approximately 29.58g (for water)
                }
                case OZ -> {
                    categoryAmount += ingredient.getAmount() * 28; // 1oz. = 28g
                }
                case CUP -> {
                    categoryAmount += ingredient.getAmount() * 128; // 1cup is approximately 128g (for flour)
                }
                default -> {
                    categoryAmount += 0;
                }
            }

            converted.put(category, categoryAmount);
        }

        return converted;
    }

    /**
     * Calculate the weights for each category as a percentage of the total weight.
     *
     * @param gramWeigths The weights for each category in grams
     * @return Returns a map of categories and their percentages
     */
    private Map<IngredientMatchingCategory, Double> mapToPercentages(Map<IngredientMatchingCategory, Double> gramWeigths) {
        LOGGER.trace("mapToPercentages({})", gramWeigths);
        var percentages = new HashMap<IngredientMatchingCategory, Double>();
        var total = gramWeigths.values().stream().mapToDouble(Double::doubleValue).sum();

        for (var entry : gramWeigths.entrySet()) {
            percentages.put(entry.getKey(), entry.getValue() / total);
        }

        return percentages;
    }
}
