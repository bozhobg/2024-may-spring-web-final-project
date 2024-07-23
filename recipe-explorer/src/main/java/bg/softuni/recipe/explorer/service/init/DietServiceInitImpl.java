package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DietServiceInitImpl {

    private final DietRepository dietRepository;

    @Autowired
    public DietServiceInitImpl(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    public void init() {
        if (dietRepository.count() > 0) return;

        List<Diet> newDiets = new ArrayList<>();

        for (DietaryType type : DietaryType.values()) {
            newDiets.add(new Diet().setType(type)
                    .setDescription(type + "\n" +
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                            "incididunt ut labore et dolore magna aliqua. Cursus turpis massa tincidunt dui. Purus " +
                            "semper eget duis at tellus. Elementum tempus egestas sed sed. Volutpat consequat mauris " +
                            "nunc congue nisi. Nunc non blandit massa enim nec. Tempor commodo ullamcorper a lacus " +
                            "vestibulum. Consectetur purus ut faucibus pulvinar elementum integer enim neque volutpat. " +
                            "Praesent semper feugiat nibh sed pulvinar proin gravida hendrerit lectus. Sit amet commodo " +
                            "nulla facilisi nullam. Eleifend mi in nulla posuere."
                    ));
        }

        this.dietRepository.saveAll(newDiets);
    }
}
