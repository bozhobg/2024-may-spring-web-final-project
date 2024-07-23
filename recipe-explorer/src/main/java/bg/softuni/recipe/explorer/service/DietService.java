package bg.softuni.recipe.explorer.service;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;

import java.util.List;

public interface DietService {
    List<DietBasicDTO> getBasicDTOs();
}
