package bg.softuni.recipe.explorer.service.impl;

import bg.softuni.recipe.explorer.model.dto.RatingDTO;
import bg.softuni.recipe.explorer.model.entity.Rating;
import bg.softuni.recipe.explorer.model.enums.RatingEnum;
import bg.softuni.recipe.explorer.repository.RatingRepository;
import bg.softuni.recipe.explorer.service.RatingService;
import bg.softuni.recipe.explorer.service.RecipeService;
import bg.softuni.recipe.explorer.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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

        Long recipeId = dto.getRecipeId();
        Optional<Rating> optExisting = this.ratingRepository.findByRecipe_IdAndUser_Id(recipeId, userId);
        Rating rating;

        if (optExisting.isPresent()) {

            rating = optExisting.get();
            rating.setRating(dto.getRating());
            this.ratingRepository.save(rating);
        } else {

            rating = new Rating()
                    .setUser(this.userService.getUserById(userId))
                    .setRating(dto.getRating())
                    .setRecipe(this.recipeService.getById(recipeId));
        }

        this.ratingRepository.save(rating);
        this.recipeService.updateAvgRating(recipeId, getAverageRating(recipeId));
    }

    @Override
    public RatingDTO getDTOByRecipeIdAndUserId(Long recipeId, Long userId) {

        Optional<Rating> optRating = this.ratingRepository.findByRecipe_IdAndUser_Id(recipeId, userId);

        if (optRating.isEmpty()) return new RatingDTO().setRecipeId(recipeId);

        RatingDTO dto = modelMapper.map(optRating.get(), RatingDTO.class);


        return dto;
    }

    @Override
    public BigDecimal getAverageRating(Long recipeId) {
        List<BigDecimal> ratingValues = this.ratingRepository.findAllByRecipe_Id(recipeId)
                .stream()
                .map(r -> mapRatingToBigDecimal(r.getRating()))
                .toList();

        BigDecimal sum = ratingValues.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        BigDecimal.compareTo is not affected by scale, while .equals will consider .0 .00 .000 scales as different
        if (sum.compareTo(BigDecimal.ZERO) == 0) return null;

        return sum.divide(BigDecimal.valueOf(ratingValues.size()), 1, RoundingMode.HALF_UP);
    }

    private BigDecimal mapRatingToBigDecimal(RatingEnum ratingEnum) {

        return switch (ratingEnum) {
            case ONE -> BigDecimal.ONE;
            case TWO -> BigDecimal.TWO;
            case THREE -> BigDecimal.valueOf(3);
            case FOUR -> BigDecimal.valueOf(4);
            case FIVE -> BigDecimal.valueOf(5);
        };
    }
}
