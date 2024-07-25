package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.RecipeShortInfoDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecipeService {

    boolean isNameUnique(String name);

    @Transactional
    List<RecipeShortInfoDTO> getAllShort();
}
