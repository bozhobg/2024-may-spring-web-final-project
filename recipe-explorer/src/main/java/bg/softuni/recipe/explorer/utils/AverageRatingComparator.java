package bg.softuni.recipe.explorer.utils;

import bg.softuni.recipe.explorer.model.entity.Recipe;

import java.math.BigDecimal;
import java.util.Comparator;

public class AverageRatingComparator implements Comparator<Recipe> {

    @Override
    public int compare(Recipe r1, Recipe r2) {

        BigDecimal avg1 = r1.getAverageRating();
        BigDecimal avg2 = r2.getAverageRating();

        if (avg1 == null) avg1 = BigDecimal.ZERO;
        if (avg2 == null) avg2 = BigDecimal.ZERO;

        return avg1.compareTo(avg2);
    }


}
