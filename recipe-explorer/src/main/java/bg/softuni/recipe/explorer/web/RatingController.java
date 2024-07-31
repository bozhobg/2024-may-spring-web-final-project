package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.RatingDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(
            RatingService ratingService
    ) {
        this.ratingService = ratingService;
    }

    @PutMapping("/add")
    public String postRating(
            @Valid RatingDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            @AuthenticationPrincipal AppUserDetails appUser
    ) {
        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, "userRating");
            return "redirect:/recipes/" + bindingModel.getRecipeId();
        }

        this.ratingService.put(bindingModel, appUser.getId());

        return "redirect:/recipes/" + bindingModel.getRecipeId();
    }
}
