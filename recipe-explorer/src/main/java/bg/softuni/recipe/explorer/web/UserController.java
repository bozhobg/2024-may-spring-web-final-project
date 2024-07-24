package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.exceptions.UserRegisterPasswordsConfirmationMismatch;
import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.service.UserService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final static String REGISTER_ATTR = "registerData";

    private final UserService userService;

    @Autowired
    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @ModelAttribute(REGISTER_ATTR)
    public UserRegisterDTO registerData() {
        return new UserRegisterDTO();
    }

    @GetMapping("/login")
    public String getLogin(
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
//        TODO: add invalid username/password How with Spr Sec?
        if (userDetails instanceof AppUserDetails) {
            return "redirect:/home";
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
//            TODO: cleaner approach
//            TODO: i18n for mismatch!

            bindingResult.addError(new FieldError(REGISTER_ATTR, "password", exc.getMessage()));
            bindingResult.addError(new FieldError(REGISTER_ATTR, "confirmPassword", exc.getMessage()));

            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, REGISTER_ATTR);

            return "redirect:/users/register";
        }


        return "redirect:/login";
    }
}
