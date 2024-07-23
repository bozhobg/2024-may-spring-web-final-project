package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.UserRegisterDTO;
import bg.softuni.recipe.explorer.service.UserService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
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

        return "redirect:/";
    }
}
