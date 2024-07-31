package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.dto.RatingDTO;
import bg.softuni.recipe.explorer.model.entity.Rating;
import bg.softuni.recipe.explorer.repository.RatingRepository;
import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final RecipeService recipeService;
    private final ModelMapper modelMapper;

    @Autowired
    public RatingServiceImpl(
            RatingRepository ratingRepository,
            UserService userService,
            RecipeService recipeService,
            ModelMapper modelMapper
    ) {
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.recipeService = recipeService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void put(RatingDTO dto, Long userId) {
        Optional<Rating> optExisting = this.ratingRepository.findByRecipe_IdAndUser_Id(dto.getRecipeId(), userId);

        if (optExisting.isPresent()) {
            Rating rating = optExisting.get();
            rating.setRating(dto.getRating());
            this.ratingRepository.save(rating);

            return;
        }

        Rating newRating = new Rating()
                .setUser(this.userService.getUserById(userId))
                .setRating(dto.getRating())
                .setRecipe(this.recipeService.getById(dto.getRecipeId()));

        this.ratingRepository.save(newRating);
    }

    @Override
    public RatingDTO getDTOByRecipeIdAndUserId(Long recipeId, Long userId) {

        Optional<Rating> optRating = this.ratingRepository.findByRecipe_IdAndUser_Id(recipeId, userId);

        if (optRating.isEmpty()) return new RatingDTO().setRecipeId(recipeId);

        RatingDTO dto = modelMapper.map(optRating.get(), RatingDTO.class);

        return dto;
    }
}
