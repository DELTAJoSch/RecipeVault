package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.WineRecommendationEngineTestData;
import at.ac.tuwien.sepm.groupphase.backend.engines.WineRecommendationEngine;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WineRecommendationEngineTest implements WineRecommendationEngineTestData {
    @Autowired
    private WineRecommendationEngine wineRecommendationEngine;

    @Test
    public void getRecommendation_whenGivenMultipleAmounts_ReturnsExpectedManuallyVerifiedResult() {
        AM_1KG.setIngredient(INGREDIENT_MEAT_WHITE);
        AM_100G.setIngredient(INGREDIENT_NIGHT_SHADOW);

        var list = new ArrayList<Amount>();
        list.add(AM_1KG);
        list.add(AM_100G);

        var result = wineRecommendationEngine.generateRecommendation(list);

        assertEquals(WineCategory.MIDDLE_RED, result.getCategory());
        assertTrue(result.getConfidence() > 0.15);
    }

    @Test
    public void getRecommendation_whenGivenClearAmounts_ReturnsExpectedManuallyVerifiedResult() {
        AM_100G.setIngredient(INGREDIENT_FISH);
        AM_1CUP.setIngredient(INGREDIENT_HERB_AROMATIC);
        AM_2LB.setIngredient(INGREDIENT_NIGHT_SHADOW);

        var list = new ArrayList<Amount>();
        list.add(AM_2LB);
        list.add(AM_100G);
        list.add(AM_2LB);

        var result = wineRecommendationEngine.generateRecommendation(list);

        assertEquals(WineCategory.MIDDLE_RED, result.getCategory());
        assertTrue(result.getConfidence() > 0.25);
    }

    @Test
    public void getRecommendation_whenGivenUndefinedCategoryOnly_ReturnsNull() {
        AM_1KG.setIngredient(INGREDIENT_UNDEFINED);

        var list = new ArrayList<Amount>();
        list.add(AM_1KG);

        var result = wineRecommendationEngine.generateRecommendation(list);

        assertNull(result);
    }
}
