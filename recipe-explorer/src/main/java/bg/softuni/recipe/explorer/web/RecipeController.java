package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.service.DietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final static String ADD_ATTR_NAME = "recipeAddData";

    private final DietService dietService;

    @Autowired
    public RecipeController(
            DietService dietService
    ) {
        this.dietService = dietService;
    }


    @ModelAttribute(ADD_ATTR_NAME)
    public RecipeAddDTO recipeAddData() {
        return new RecipeAddDTO();
    }

    @ModelAttribute("mealTypes")
    public MealType[] mealTypes() {
        return MealType.values();
    }

    @ModelAttribute("dietsData")
    public List<DietBasicDTO> dietsData() {
        return this.dietService.getBasicDTOs();
    }

    @GetMapping("/add")
    public String getAdd() {

        return "recipe-add";
    }
}
