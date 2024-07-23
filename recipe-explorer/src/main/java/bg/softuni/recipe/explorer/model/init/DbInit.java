package bg.softuni.recipe.explorer.model.init;

import bg.softuni.recipe.explorer.repository.DietRepository;
import bg.softuni.recipe.explorer.service.init.DietServiceInitImpl;
import bg.softuni.recipe.explorer.service.init.RoleServiceInitImpl;
import bg.softuni.recipe.explorer.service.init.UserServiceInitImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {

    private final RoleServiceInitImpl roleServiceInit;
    private final UserServiceInitImpl userServiceInit;
    private final DietServiceInitImpl dietServiceInit;

    @Autowired
    public DbInit(
            RoleServiceInitImpl roleServiceInit,
            UserServiceInitImpl userServiceInit,
            DietServiceInitImpl dietServiceInit
    ) {
        this.roleServiceInit = roleServiceInit;
        this.userServiceInit = userServiceInit;
        this.dietServiceInit = dietServiceInit;
    }

    @Override
    public void run(String... args) throws Exception {
        roleServiceInit.init();
        userServiceInit.init();
        dietServiceInit.init();
    }

}
