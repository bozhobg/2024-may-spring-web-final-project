package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.dto.DietBasicDTO;
import bg.softuni.recipe.explorer.repository.DietRepository;
import bg.softuni.recipe.explorer.service.DietService;
import bg.softuni.recipe.explorer.utils.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

//    private String mapStringToUpperCase(String value) {
//
//        return Arrays.stream(value.split("_"))
//                .map(String::toLowerCase)
//                .map(s -> {
//                    char firstChar = Character.toUpperCase(s.charAt(0));
//                    return firstChar + s.substring(1);
//                })
//                .collect(Collectors.joining(" "));
//    }
}
