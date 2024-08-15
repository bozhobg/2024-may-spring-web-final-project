package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.constants.SortingEnum;
import bg.softuni.recipe.explorer.model.dto.*;
import bg.softuni.recipe.explorer.model.enums.MealType;
import bg.softuni.recipe.explorer.model.enums.RatingEnum;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.CommentService;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final static String ADD_EDIT_ATTR = "recipeAddData";

    private final RecipeService recipeService;
    private final DietService dietService;
    private final RatingService ratingService;
    private final CommentService commentService;

    @Autowired
    public RecipeController(
            RecipeService recipeService,
            DietService dietService,
            RatingService ratingService,
            CommentService commentService
    ) {
        this.recipeService = recipeService;
        this.dietService = dietService;
        this.ratingService = ratingService;
        this.commentService = commentService;
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

    @ModelAttribute("sortType")
    public SortingEnum[] sortType() {
        return SortingEnum.values();
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

    //    TODO:
//    @GetMapping("/search")
//    public String searchRecipes(
//            @RequestParam String names,
//            @RequestParam String ingredients,
//            @RequestParam(required = false) MealType mealType,
//            @RequestParam(required = false) DietaryType dietType,
//            Model model
//    ) {
//
//        this.recipeService.search(names, ingredients, mealType, dietType);
//
//        return "recipes-all";
//    }

    @GetMapping("/filter")
    public String getFilter(
            @RequestParam(required = false) MealType mealType,
            @RequestParam(required = false) Long dietId,
            @RequestParam(required = false) SortingEnum ratingSort,
            Model model
    ) {
        model.addAttribute("all", this.recipeService.filter(mealType, dietId, ratingSort));

        return "recipes-all";
    }

    @GetMapping("/{id}")
    public String getDetails(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) throws JsonProcessingException {


        model.addAttribute("recipe", this.recipeService.getDetailsById(id));
        model.addAttribute("ratings", RatingEnum.values());

        if (!model.containsAttribute("commentPutData")) {
            model.addAttribute("commentPutData", new CommentPutDTO());
        }

//        TODO: resolve rest client handling
        try {
            model.addAttribute("comments", this.commentService.getCommentsForRecipe(id));
        } catch (RestClientException rce) {
            model.addAttribute("comments", new ArrayList<CommentViewDTO>());
        }

        if (!model.containsAttribute("userRating")) {
            model.addAttribute("userRating",
                    this.ratingService.getDTOByRecipeIdAndUserId(id, appUserDetails.getId()));
        }

        return "recipe-details";
    }


    //    @ResponseStatus(HttpStatus.NO_CONTENT) -> prevents redirect on client side!
    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        this.recipeService.delete(id, appUserDetails);

        return "redirect:/recipes/all";
    }

    @GetMapping("/{id}/edit")
    public String getEdit(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        if (!model.containsAttribute("recipeEditData")) {
            model.addAttribute("recipeEditData", this.recipeService.getEditDTO(id, appUserDetails));
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

        this.recipeService.put(recipeId, bindingModel, appUserDetails);

        return "redirect:/recipes/" + recipeId;
    }
}
