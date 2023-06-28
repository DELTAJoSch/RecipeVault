package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AmountRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.GroceryListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class DefaultGroceryListService implements GroceryListService {
    private AmountRepository amountRepository;

    private UserRepository userRepository;

    private RecipeRepository recipeRepository;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DefaultGroceryListService(AmountRepository amountRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.amountRepository = amountRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Amount> generateGroceryList(String email, Long[][] portionsPerRecipe) {
        LOGGER.trace("generateGroceryList({}, {})", email, portionsPerRecipe);
        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden");
        }

        List<Amount> amounts = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        Optional<Recipe> recipe;
        for (int i = 0; i < portionsPerRecipe.length; i++) {
            recipe = recipeRepository.findById(portionsPerRecipe[i][0]);
            if (recipe.isEmpty()) {
                throw new NotFoundException("Rezept konnte nicht gefunden werden");
            }
            long portions = portionsPerRecipe[i][1];
            if (portions == 0) {
                continue;
            }
            List<Amount> amountsOfRecipe = recipe.get().getIngredients();
            for (Amount a : amountsOfRecipe) {
                Ingredient ing = a.getIngredient();
                if (ingredients.contains(ing)) {
                    List<Amount> storedAmounts = amounts.stream().filter(amount -> amount.getIngredient().equals(ing)).toList();
                    if (a.getUnit() != AmountUnit.PIECE && a.getUnit() != AmountUnit.CUP) {
                        boolean stored = false;
                        for (Amount storedAmount : storedAmounts) {
                            if (storedAmount.getUnit() != AmountUnit.PIECE && storedAmount.getUnit() != AmountUnit.CUP) {
                                amounts.set(amounts.indexOf(storedAmount), new Amount(recipe.get(), ing, storedAmount.getAmount() + getInGrams(a) * portions, AmountUnit.G));
                                stored = true;
                                break;
                            }
                        }
                        if (!stored) {
                            amounts.add(new Amount(recipe.get(), ing, getInGrams(a) * portions, AmountUnit.G));
                        }
                    } else {
                        boolean stored = false;
                        for (Amount storedAmount : storedAmounts) {
                            if (storedAmount.getUnit() == a.getUnit()) {
                                amounts.set(amounts.indexOf(storedAmount), new Amount(recipe.get(), ing, storedAmount.getAmount() + a.getAmount() * portions, storedAmount.getUnit()));
                                stored = true;
                                break;
                            }
                        }
                        if (!stored) {
                            amounts.add(new Amount(recipe.get(), ing, a.getAmount() * portions, a.getUnit()));
                        }
                    }
                } else {
                    ingredients.add(ing);
                    if (a.getUnit() != AmountUnit.CUP && a.getUnit() != AmountUnit.PIECE) {
                        amounts.add(new Amount(recipe.get(), ing, getInGrams(a) * portions, AmountUnit.G));
                    } else {
                        amounts.add(new Amount(recipe.get(), ing, a.getAmount() * portions, a.getUnit()));
                    }
                }
            }
        }
        amounts.sort(Comparator.comparing((Amount a) -> a.getIngredient().getName()));
        convertToKg(amounts);
        return amounts;
    }


    /**
     * convert every amount that has the unit grams and is over 1000 to kg.
     *
     * @param amounts list of amounts
     */
    private void convertToKg(List<Amount> amounts) {
        LOGGER.trace("convertToKg({})", amounts);
        for (int i = 0; i < amounts.size(); i++) {
            Amount a = amounts.get(i);
            if (a.getUnit() == AmountUnit.G && a.getAmount() >= 1000) {
                a.setAmount(a.getAmount() / 1000);
                a.setUnit(AmountUnit.KG);
            }
        }
    }

    /**
     * Convert from another unit into grams.
     *
     * @param a amount to convert into grams
     * @return amount in grams
     */
    private Double getInGrams(Amount a) {
        LOGGER.trace("getInGrams({})", a);
        switch (a.getUnit()) {
            case KG -> {
                return a.getAmount() * 1000;
            }
            case OZ -> {
                return a.getAmount() * 28.5;
            }
            case LB -> {
                return (a.getAmount() * 453.5);
            }
            case Fl -> {
                return (a.getAmount() * 29.5);
            }
            case TSP -> {
                return (a.getAmount() * 4.5);
            }
            case TBSP -> {
                return a.getAmount() * 15;
            }
            default -> {
                return a.getAmount();
            }
        }
    }
}
