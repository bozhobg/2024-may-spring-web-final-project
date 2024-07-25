package bg.softuni.recipe.explorer.init;

import bg.softuni.recipe.explorer.service.init.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {

    private final RoleInitServiceImpl roleInitService;
    private final UserInitServiceImpl userInitService;
    private final DietInitServiceImpl dietInitService;
    private final IngredientInitServiceImpl ingredientInitService;
    private final RecipeInitServiceImpl recipeInitService;

    @Autowired
    public DbInit(
            RoleInitServiceImpl roleInitService,
            UserInitServiceImpl userInitService,
            DietInitServiceImpl dietInitService,
            IngredientInitServiceImpl ingredientInitService,
            RecipeInitServiceImpl recipeInitService
    ) {
        this.roleInitService = roleInitService;
        this.userInitService = userInitService;
        this.dietInitService = dietInitService;
        this.ingredientInitService = ingredientInitService;
        this.recipeInitService = recipeInitService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleInitService.init();
        userInitService.init();
        dietInitService.init();
        ingredientInitService.init();
        recipeInitService.init();
    }

}
