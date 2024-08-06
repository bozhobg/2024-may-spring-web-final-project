package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.constants.ExceptionMessages;
import bg.softuni.recipe.explorer.exceptions.ObjectNotFoundException;
import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
import bg.softuni.recipe.explorer.model.enums.DietaryType;
import bg.softuni.recipe.explorer.repository.DietRepository;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DietServiceImpl implements DietService {

    private final DietRepository dietRepository;

    @Autowired
    public DietServiceImpl(
            DietRepository dietRepository
    ) {
        this.dietRepository = dietRepository;
    }

    @Override
    public List<DietBasicDTO> getBasicDTOs() {
//        TODO: cache

        return this.dietRepository.findAll()
                .stream()
                .map(this::mapToBasic)
                .toList();
    }

    @Override
    public boolean areIdsValid(List<Long> listIds) {

        for (Long id : listIds) {
            if (!this.dietRepository.existsById(id)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Set<Diet> getAllByIds(List<Long> listIds) {
        List<Diet> allById = this.dietRepository.findAllById(listIds);

        return new HashSet<>(allById);
    }

    @Override
    public Diet getByType(DietaryType dietType) {
        return this.dietRepository.findByDietaryType(dietType)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.DIET_NOT_FOUND));
    }

    @Override
    public Diet getById(Long id) {
        return this.dietRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ExceptionMessages.DIET_NOT_FOUND));
    }

    private DietBasicDTO mapToBasic(Diet entity) {
        return new DietBasicDTO()
                .setId(entity.getId())
                .setType(
                        StringFormatter.mapConstantCaseToUpperCase(entity.getDietaryType().name())
                );
    }
}
