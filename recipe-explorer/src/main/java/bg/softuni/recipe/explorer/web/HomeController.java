package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {


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
    public String getHome() {

        return "index";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }
}
