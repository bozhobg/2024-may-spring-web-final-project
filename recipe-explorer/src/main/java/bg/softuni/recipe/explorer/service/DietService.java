package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.enums.DietaryType;

import java.util.List;
import java.util.Set;

public interface DietService {
    List<DietBasicDTO> getBasicDTOs();

    boolean areIdsValid(List<Long> listIds);

    Set<Diet> getAllByIds(List<Long> listIds);

    Diet getByType(DietaryType dietType);

    Diet getById(Long id);
}
