package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DietInitServiceImpl {

    private final static String LOREM_IPSUM =  "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
            "eiusmod tempor incididunt ut labore et dolore magna aliqua. Cursus turpis massa tincidunt dui. Purus " +
            "semper eget duis at tellus. Elementum tempus egestas sed sed. Volutpat consequat mauris " +
            "nunc congue nisi.";

    private final DietRepository dietRepository;

    @Autowired
    public DietInitServiceImpl(DietRepository dietRepository) {
        this.dietRepository = dietRepository;
    }

    public void init() {
        if (dietRepository.count() > 0) return;

        List<Diet> newDiets = new ArrayList<>();

        for (DietaryType type : DietaryType.values()) {
            newDiets.add(new Diet().setType(type)
                    .setDescription(type + "\n" + LOREM_IPSUM));
        }

        this.dietRepository.saveAll(newDiets);
    }

    Set<Diet> getRandomCountOfDiets(int count) {
        Set<Diet> randomDiets = new HashSet<>();

        for (int i = 0; i < count; i++) {
            randomDiets.add(getRandomDiet());
        }

        return randomDiets;
    }

    Diet getRandomDiet() {
        long count = this.dietRepository.count();
        Random random = new Random();
        long randomDietIndex = random.nextLong(count) + 1;

        return this.dietRepository.findById(randomDietIndex)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid random diet during DB init!"));
    }
}
