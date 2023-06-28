package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.AmountMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.GroceryListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Endpoint for grocery lists of lists.
 */
@RestController
@RequestMapping(value = "/api/v1/grocerylist")
public class GroceryListEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroceryListService groceryListService;
    private final AmountMapper amountMapper;


    @Autowired
    public GroceryListEndpoint(GroceryListService groceryListService, AmountMapper amountMapper) {
        this.groceryListService = groceryListService;
        this.amountMapper = amountMapper;
    }

    /**
     * Generate a grocery list.
     *
     * @param portionsPerRecipe array containing the number of portions per recipe(id)
     * @param authentication    the issuing user
     * @return a condensed list of amounts for the given recipes
     */
    @Secured("ROLE_USER")
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "generate a grocery list", security = @SecurityRequirement(name = "apiKey"))
    public List<AmountDto> generateGroceryList(@RequestBody Long[][] portionsPerRecipe, Authentication authentication) {
        LOGGER.info("POST /api/v1/grocerylist");
        return amountMapper.amountToAmountDto(groceryListService.generateGroceryList(authentication.getName(), portionsPerRecipe));
    }

}
