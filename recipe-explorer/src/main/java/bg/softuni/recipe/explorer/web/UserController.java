package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.exceptions.UserRegisterPasswordsConfirmationMismatch;
import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.model.dto.UserUsernameDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.service.UserService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final static String REGISTER_ATTR = "registerData";

    private final UserService userService;
    private final RecipeService recipeService;

    @Autowired
    public UserController(
            UserService userService,
            RecipeService recipeService
    ) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @ModelAttribute(REGISTER_ATTR)
    public UserRegisterDTO registerData() {
        return new UserRegisterDTO();
    }

    @GetMapping("/login")
    public String getLogin(
            @AuthenticationPrincipal AppUserDetails userDetails,
            Model model,
            @RequestParam(required = false) boolean error
    ) {
//        TODO: add invalid username/password How with Spr Sec?
        if (userDetails instanceof AppUserDetails) {
            return "redirect:/home";
        }

        if (!model.containsAttribute("isUsernameChanged")) {
            model.addAttribute("isUsernameChanged", false);
        }
        if (error) {
            model.addAttribute("error", true);
        }

        return "auth-login";
    }

    @GetMapping("/register")
    public String getRegister(
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        if (userDetails instanceof AppUserDetails) {
            return "redirect:/home";
        }
        return "auth-register";
    }

    @PostMapping("/register")
    public String postRegister(
            @Valid UserRegisterDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs
    ) {
        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, REGISTER_ATTR);

            return "redirect:/users/register";
        }

        try {
            this.userService.doRegister(bindingModel);
        } catch (UserRegisterPasswordsConfirmationMismatch exc) {
//            TODO: cleaner approach -> with custom Type @nno! workshop
//            TODO: i18n for mismatch!

            bindingResult.addError(new FieldError(REGISTER_ATTR, "password", exc.getMessage()));
            bindingResult.addError(new FieldError(REGISTER_ATTR, "confirmPassword", exc.getMessage()));

            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, REGISTER_ATTR);

            return "redirect:/users/register";
        }


        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String getProfile(
            Model model,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {

        model.addAttribute(
                "userData",
                this.userService.getProfileDataByUsername(appUserDetails.getUsername()));
        model.addAttribute(
                "userRecipes",
                this.recipeService.getAllBasicByUserId(appUserDetails.getId()));

        if (!model.containsAttribute("usernameData")) {
            model.addAttribute("usernameData", new UserUsernameDTO());
        }

        return "user-profile";
    }

    @GetMapping("/{username}")
    public String getUserProfile(
            Model model,
            @PathVariable String username,
            @AuthenticationPrincipal AppUserDetails appUserDetails
    ) {
        if (appUserDetails.getUsername().equals(username)) return "redirect:/users/profile";

        model.addAttribute(
                "userData",
                this.userService.getProfileDataByUsername(username));
        model.addAttribute(
                "userRecipes",
                this.recipeService.getAllBasicByUserId(appUserDetails.getId()));

        return "user-profile";
    }

    @PatchMapping("/profile/username")
    public String changeUsername(
            @Valid UserUsernameDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs,
            @AuthenticationPrincipal AppUserDetails appUserDetails,
            HttpServletRequest request
    ) throws ServletException {
        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, "usernameData");
            return "redirect:/users/profile";
        }

        this.userService.patchUsername(bindingModel.getUsername(), appUserDetails);

//        manually logging out user, user details not updated after change of username
//        TODO: how to reload user details with new username
        request.logout();

        rAttrs.addFlashAttribute("isUsernameChanged", true);

        return "redirect:/users/login";
    }
}
