package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.dto.IngredientShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.repository.IngredientRepository;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientServiceImpl(
            IngredientRepository ingredientRepository,
            ModelMapper modelMapper
    ) {
        this.ingredientRepository = ingredientRepository;
        this.modelMapper = modelMapper;
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

    @Override
    public List<IngredientShortInfoDTO> getAllShort() {
        List<IngredientShortInfoDTO> allShort = this.ingredientRepository.findAll()
                .stream()
                .map(this::mapToShort)
                .toList();

        return allShort;
    }

    private IngredientShortInfoDTO mapToShort(Ingredient entity) {
        return modelMapper.map(entity, IngredientShortInfoDTO.class);
    }
}
