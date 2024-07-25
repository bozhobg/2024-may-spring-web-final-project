package bg.softuni.recipe.explorer.web;

import bg.softuni.recipe.explorer.model.dto.IngredientAddDTO;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.utils.RedirectUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final static String ADD_ATTR = "ingredientAddData";

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(
            IngredientService ingredientService
    ) {
        this.ingredientService = ingredientService;
    }

    @ModelAttribute(ADD_ATTR)
    public IngredientAddDTO addData() {
        return new IngredientAddDTO();
    }

    @ModelAttribute("ingredientTypes")
    public Map<IngredientType, String> ingredientTypes() {
        return this.ingredientService.getMapTypeString();
    }

    @ModelAttribute("unitTypes")
    public UnitEnum[] unitTypes() {
        return UnitEnum.values();
    }

    @GetMapping("/add")
    public String getAdd() {

        return "ingredient-add";
    }

    @PostMapping("/add")
    public String postAdd(
            @Valid IngredientAddDTO bindingModel,
            BindingResult bindingResult,
            RedirectAttributes rAttrs
    ) {
        if (bindingResult.hasErrors()) {
            RedirectUtil.setRedirectAttrs(rAttrs, bindingModel, bindingResult, ADD_ATTR);

            return "redirect:/ingredients/add";
        }

        return "redirect:/";
    }

    @GetMapping("/all")
    public String getAll(
            Model model
    ) {

        model.addAttribute("all", this.ingredientService.getAllShort());

        return "ingredients-all";
    }

    @GetMapping("/{id}")
    public String getDetails(
            @PathVariable Long id
    ) {

        return "ingredient-details";
    }
}
