package at.ac.tuwien.sepm.groupphase.backend.engines;

import java.io.File;
import java.util.List;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;

/*
 * engine to do ocr, and add extract ingredients from it
 */
public interface OcrEngine {

    /**
     * OCR image of Recipe.
     *
     * @param imageFile  image of the recipe
     * @return rext of the recipe
     */
    String ocrFile(File imageFile);

    /**
     * extract ingredients from recipe list.
     *
     * @param recipeString takes text of the recipe
     * @return returns list of amounts
     */
    List<AmountDto> extractIngredientList(String recipeString);

}
