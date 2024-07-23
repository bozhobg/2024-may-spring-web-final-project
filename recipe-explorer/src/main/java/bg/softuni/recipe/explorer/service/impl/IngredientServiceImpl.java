package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.repository.IngredientRepository;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientServiceImpl(
            IngredientRepository ingredientRepository
    ) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public boolean isNameUnique(String s) {
        return !this.ingredientRepository.existsByName(s);
    }


    @Override
    public Map<IngredientType, String> getMapTypeString() {

        Map<IngredientType, String> mapTypeFormatted = new TreeMap<>();

        for (IngredientType value : IngredientType.values()) {
            mapTypeFormatted.put(value, StringFormatter.mapConstantCaseToUpperCase(value.name()));
        }

        return mapTypeFormatted;
    }
}
