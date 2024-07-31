package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.model.enums.RatingEnum;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final RatingService ratingService;

    @Autowired
    public RecipeController(
            RecipeService recipeService,
            DietService dietService,
            RatingService ratingService
    ) {
        this.recipeService = recipeService;
        this.dietService = dietService;
        this.ratingService = ratingService;
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
            RedirectAttributes rAttrs,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {

        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, ADD_ATTR);

            return "redirect:/recipes/add";
        }

        Long newId = this.recipeService.add(bindingModel, userDetails.getId());

        return "redirect:/recipes/" + newId;
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
            Model model,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        model.addAttribute("recipe", this.recipeService.getDetailsById(id));
        model.addAttribute("ratings", RatingEnum.values());

        if (!model.containsAttribute("userRating")) {
            model.addAttribute("userRating",
                    this.ratingService.getDTOByRecipeIdAndUserId(id, appUserDetails.getId()));
        }

        return "recipe-details";
    }

//    TODO: put/patch, delete functionality
}
