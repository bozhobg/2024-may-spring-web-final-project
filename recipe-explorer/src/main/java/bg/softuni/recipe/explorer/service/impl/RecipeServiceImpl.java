package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.repository.RecipeRepository;
import bg.softuni.recipe.explorer.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public boolean isUnique(String name) {
        return !this.recipeRepository.existsByName(name);
    }
}
