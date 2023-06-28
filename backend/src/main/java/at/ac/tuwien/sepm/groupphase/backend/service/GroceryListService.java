package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;

import java.util.List;

public interface GroceryListService {
    /**
     * Generate a grocery list, containing the amount needed of each ingredient.
     *
     * @param email             of issuing user
     * @param portionsPerRecipe contains the number of portions for each recipe
     * @return a condensed list of the amounts of ingredients needed
     */
    List<Amount> generateGroceryList(String email, Long[][] portionsPerRecipe);
}
