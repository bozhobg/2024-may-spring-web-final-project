package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final RecipeService recipeService;
    private final DietService dietService;

    @Autowired
    public HomeController(
            RecipeService recipeService,
            DietService dietService
    ) {
        this.recipeService = recipeService;
        this.dietService = dietService;
    }

    @GetMapping
    public String getIndex(
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        if (userDetails instanceof AppUserDetails) { // smart cast with instanceof
            return "redirect:/home";
        }

        return "index";
    }

    @GetMapping("/home")
    public String getHome(
            Model model,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {

        model.addAttribute("userFullName", appUserDetails.getFullName());
        model.addAttribute("topRated", this.recipeService.getHighestRated());
        model.addAttribute("randomToday", this.recipeService.getRandomForTheDay());
        model.addAttribute("lastAdded", this.recipeService.getLastAdded());
        model.addAttribute("diets", dietService.getBasicDTOs());
        model.addAttribute("meals", MealType.values());

        return "home";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }
}
