package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.enums.IngredientType;

import java.util.Map;

public interface IngredientService {

    boolean isNameUnique(String s);

    Map<IngredientType, String> getMapTypeString();
}
