package bg.softuni.recipe.explorer.service.init;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.enums.UnitEnum;
import bg.softuni.recipe.explorer.repository.IngredientRepository;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IngredientInitServiceImpl {

    private static final int RANDOM_BOUND = 5;
    private static final String LOREM_IPSUM = "Lorem ipsum odor amet, consectetuer adipiscing elit. Et praesent penatibus varius natoque auctor eros nibh. Mi sollicitudin per nec a eu parturient facilisis vivamus proin.";

    private final IngredientRepository ingredientRepository;
    private final UserInitServiceImpl userInitService;

    @Autowired
    public IngredientInitServiceImpl(
            IngredientRepository ingredientRepository,
            UserInitServiceImpl userInitService
    ) {
        this.ingredientRepository = ingredientRepository;
        this.userInitService = userInitService;
    }

    public void init() {
        if (this.ingredientRepository.count() > 0) return;

        List<Ingredient> newIngredients = new ArrayList<>();

        Random random = new Random();

        for (IngredientType value : IngredientType.values()) {
            int ingCount = random.nextInt(RANDOM_BOUND) + 1;

            for (int i = 1; i <= ingCount; i++) {

                String name = StringFormatter.mapConstantCaseToUpperCase(value.name()) + " " + i;

                newIngredients.add(new Ingredient()
                        .setName(name)
                        .setDescription(i + " " + LOREM_IPSUM)
                        .setAddedBy(userInitService.getRandomUser())
                        .setType(value)
                        .setUnit(UnitEnum.values()[random.nextInt(2)]));
            }
        }

        this.ingredientRepository.saveAll(newIngredients);
    }

    Set<Ingredient> getRandomCountOfIngredients(int count) {

        Set<Ingredient> randomList = new HashSet<>();

        for (int i = 0; i < count; i++) {
            randomList.add(getRandomIngredient());
        }

        return randomList;
    }

    Ingredient getRandomIngredient() {

        Random random = new Random();
        long ingIndexBound = this.ingredientRepository.count();
        long randomIngIndex = random.nextLong(ingIndexBound) + 1;

        return this.ingredientRepository.findById(randomIngIndex)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid random ingredient during DB init!"));
    }
}
