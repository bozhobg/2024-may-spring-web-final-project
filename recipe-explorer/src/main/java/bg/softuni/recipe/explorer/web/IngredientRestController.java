package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.IngredientBasicDTO;
import bg.softuni.recipe.explorer.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientRestController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientRestController(
            IngredientService ingredientService
    ) {
        this.ingredientService = ingredientService;
    }

//    TODO: secure rest controller endpoints

    @GetMapping("/short")
    public List<IngredientBasicDTO> getAllBasic() {

        return this.ingredientService.getAllBasic();
    }
}
