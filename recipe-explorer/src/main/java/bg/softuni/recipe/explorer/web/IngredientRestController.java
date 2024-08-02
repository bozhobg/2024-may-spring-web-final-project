package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.IngredientBasicDTO;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientRestController {

    private final IngredientService ingredientService;
    private final RecipeService recipeService;

    @Autowired
    public IngredientRestController(
            IngredientService ingredientService,
            RecipeService recipeService
    ) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
    }

//    TODO: secure rest controller endpoints

    @GetMapping("/short")
    public ResponseEntity<List<IngredientBasicDTO>> getAllBasic() {

        List<IngredientBasicDTO> allBasicDTOs = this.ingredientService.getAllBasic();
        return ResponseEntity.ok(allBasicDTOs);
    }

    @GetMapping("/short/recipe/{id}")
    public ResponseEntity<List<IngredientBasicDTO>> getAllBasicForRecipe(
            @PathVariable("id") Long recipeId
    ) {
        List<IngredientBasicDTO> recipeBasicDTOs = this.ingredientService.getAllBasicForRecipeId(recipeId);
        return ResponseEntity.ok(recipeBasicDTOs);
    }

//    TODO: put/patch, delete functionality
}
