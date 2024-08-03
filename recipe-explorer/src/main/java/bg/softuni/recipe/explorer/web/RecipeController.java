package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeAddDTO;
import bg.softuni.recipe.explorer.model.dto.RecipeEditDTO;
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

    private final static String ADD_EDIT_ATTR = "recipeAddData";

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


    @ModelAttribute(ADD_EDIT_ATTR)
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

//    TODO: status code created 201
    @PostMapping("/add")
    public String postAdd(
            @Valid RecipeAddDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {

        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, ADD_EDIT_ATTR);

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


//    @ResponseStatus(HttpStatus.NO_CONTENT) -> prevents redirect on client side!
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id
    ) {
        this.recipeService.delete(id);

        return "redirect:/recipes/all";
    }

    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable Long id,
            Model model
    ) {
        if (!model.containsAttribute("recipeEditData")) {
            model.addAttribute("recipeEditData", this.recipeService.getEditDTO(id));
        }
        model.addAttribute("recipeId", id);

        return "recipe-edit";
    }

//    TODO: put or patch?

    @PutMapping("/{recipeId}/edit")
    public String postEdit(
            @PathVariable Long recipeId,
            @Valid RecipeEditDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {

//            TODO: validate new name, without checking current

        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, "recipeEditData");

            return "redirect:/recipes/" + recipeId + "/edit";
        }

        this.recipeService.put(recipeId, bindingModel);

        return "redirect:/recipes/" + recipeId;
    }
}
