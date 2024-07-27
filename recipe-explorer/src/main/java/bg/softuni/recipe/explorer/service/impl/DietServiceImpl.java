package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.model.entity.Diet;
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
                .map(e -> new DietBasicDTO()
                        .setId(e.getId())
                        .setType(StringFormatter.mapConstantCaseToUpperCase(e.getType().name())))
                .toList();
    }

    @Override
    public boolean areIdsValid(List<Long> listIds) {
        boolean exist = false;

        for (Long id : listIds) {
            exist = this.dietRepository.existsById(id);

            if (!exist) return exist;
        }

        return exist;
    }

    @Override
    public Set<Diet> getAllByIds(List<Long> listIds) {
        List<Diet> allById = this.dietRepository.findAllById(listIds);

        return new HashSet<>(allById);
    }
}
