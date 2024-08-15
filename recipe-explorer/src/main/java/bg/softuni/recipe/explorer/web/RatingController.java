package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.RatingDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    @PutMapping("/put")
    public String putRating(
            @Valid RatingDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            @AuthenticationPrincipal AppUserDetails appUser,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, "userRating");
            String referer = request.getHeader("Referer");

//            using referer instead recipe id in case id is invalid (altered)
            return "redirect:" + referer;
        }

        this.ratingService.put(bindingModel, appUser.getId());

        return "redirect:/recipes/" + bindingModel.getRecipeId();
    }
}
