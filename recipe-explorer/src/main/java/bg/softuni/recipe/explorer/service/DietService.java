package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;

import java.util.List;
import java.util.Set;

public interface DietService {
    List<DietBasicDTO> getBasicDTOs();

    boolean areIdsValid(List<Long> listIds);

    Set<Diet> getAllByIds(List<Long> listIds);
}
