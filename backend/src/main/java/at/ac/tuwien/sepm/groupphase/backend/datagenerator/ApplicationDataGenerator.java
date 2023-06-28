package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * This data generator resolves the dependencies between the other generators.
 */
@Profile("generateData")
@Component
public class ApplicationDataGenerator {

    private final UserDataGenerator userDataGenerator;
    private final WineDataGenerator wineDataGenerator;
    private final RecipeDataGenerator recipeDataGenerator;
    private final OptionsDataGenerator optionsDataGenerator;
    private final IngredientDataGenerator ingredientDataGenerator;
    private final AuthorDataGenerator authorDataGenerator;

    public ApplicationDataGenerator(
        UserDataGenerator userDataGenerator,
        WineDataGenerator wineDataGenerator,
        RecipeDataGenerator recipeDataGenerator,
        OptionsDataGenerator optionsDataGenerator,
        IngredientDataGenerator ingredientDataGenerator,
        AuthorDataGenerator authorDataGenerator) {
        this.userDataGenerator = userDataGenerator;
        this.wineDataGenerator = wineDataGenerator;
        this.recipeDataGenerator = recipeDataGenerator;
        this.optionsDataGenerator = optionsDataGenerator;
        this.ingredientDataGenerator = ingredientDataGenerator;
        this.authorDataGenerator = authorDataGenerator;
    }

    /**
     * Generates the data. Resolves dependencies through order of insertion!
     */
    @PostConstruct
    private void generate() {
        this.userDataGenerator.generate();
        this.wineDataGenerator.generate();
        this.optionsDataGenerator.generate();
        this.ingredientDataGenerator.generate();
        this.authorDataGenerator.generate();
        this.recipeDataGenerator.generate();
    }
}
