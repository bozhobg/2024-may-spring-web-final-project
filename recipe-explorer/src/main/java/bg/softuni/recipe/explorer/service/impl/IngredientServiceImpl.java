package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.dto.IngredientAddDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientBasicDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientDetailsDTO;
import bg.softuni.recipe.explorer.model.dto.IngredientShortInfoDTO;
import bg.softuni.recipe.explorer.model.entity.Ingredient;
import bg.softuni.recipe.explorer.model.entity.User;
import bg.softuni.recipe.explorer.model.enums.IngredientType;
import bg.softuni.recipe.explorer.model.user.AppUserDetails;
import bg.softuni.recipe.explorer.repository.IngredientRepository;
import bg.softuni.recipe.explorer.service.IngredientService;
import bg.softuni.recipe.explorer.service.UserService;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientServiceImpl(
            IngredientRepository ingredientRepository,
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.ingredientRepository = ingredientRepository;
        this.userService = userService;
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

    @Override
    public IngredientDetailsDTO getDetailsById(Long id) {
        IngredientDetailsDTO map = mapToIngredientDetailsDTO(this.ingredientRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Ingredient not found!")));

        return map;
    }

    @Override
    public List<IngredientBasicDTO> getAllBasic() {
        List<IngredientBasicDTO> allBasic = this.ingredientRepository.findAll()
                .stream()
                .map(this::mapToBasic)
                .toList();

        return allBasic;
    }

    @Override
    public boolean areIdsValid(List<Long> listIds) {

        boolean exist = false;

        for (Long id : listIds) {
            exist = this.ingredientRepository.existsById(id);

            if (!exist) return exist;
        }

        return exist;
    }

    @Override
    public Set<Ingredient> getAllByIds(List<Long> listIds) {
        List<Ingredient> allById = this.ingredientRepository.findAllById(listIds);

        return new HashSet<>(allById);
    }

    @Override
    public Long add(IngredientAddDTO dto, Long userId) {
        Ingredient newIngredient = mapToEntity(dto, userId);

        return this.ingredientRepository
                .save(newIngredient)
                .getId();
    }

    private Ingredient mapToEntity(IngredientAddDTO dto, Long userId) {
        Ingredient map = modelMapper.map(dto, Ingredient.class);
        User user = this.userService.getUserById(userId);
        map.setAddedBy(user);

        return map;
    }

    private IngredientShortInfoDTO mapToShort(Ingredient entity) {
        return modelMapper.map(entity, IngredientShortInfoDTO.class);
    }

    private IngredientDetailsDTO mapToIngredientDetailsDTO(Ingredient entity) {
        return modelMapper.map(entity, IngredientDetailsDTO.class);
    }

    private IngredientBasicDTO mapToBasic(Ingredient entity) {
        return modelMapper.map(entity, IngredientBasicDTO.class);
    }
}
