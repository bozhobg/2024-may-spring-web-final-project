package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final static String ADD_ATTR = "recipeAddData";

    private final RecipeService recipeService;
    private final DietService dietService;

    @Autowired
    public RecipeController(
            RecipeService recipeService,
            DietService dietService
    ) {
        this.recipeService = recipeService;
        this.dietService = dietService;
    }


    @ModelAttribute(ADD_ATTR)
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

    @PostMapping("/add")
    public String postAdd(
            @Valid RecipeAddDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs
    ) {

        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, ADD_ATTR);

            return "redirect:/recipes/add";
        }

        return "redirect:/";
    }

    @GetMapping("/all")
    public String getAll(
            Model model
    ) {
        model.addAttribute("all", this.recipeService.getAllShort());

        return "recipes-all";
    }

    @GetMapping("/{id}")
    public String getDetails(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("recipe", this.recipeService.getDetailsById(id));

        return "recipe-details";
    }
}
