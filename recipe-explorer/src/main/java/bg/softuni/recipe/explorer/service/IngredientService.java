package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.IngredientAddDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientBasicDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.IngredientType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IngredientService {

    boolean isNameUnique(String s);

    Map<IngredientType, String> getMapTypeString();

    List<IngredientShortInfoDTO> getAllShort();

    IngredientDetailsDTO getDetailsById(Long id);

    List<IngredientBasicDTO> getAllBasic();

    boolean areIdsValid(List<Long> listIds);

    Set<Ingredient> getAllByIds(List<Long> listIds);

    Long add(IngredientAddDTO dto, Long userId);
}
