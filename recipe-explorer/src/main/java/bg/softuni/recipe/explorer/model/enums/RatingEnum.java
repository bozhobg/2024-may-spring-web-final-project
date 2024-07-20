package bg.softuni.recipe.explorer.model.enums;

import java.math.BigDecimal;

public enum RatingEnum {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int rating;

    RatingEnum(int rating) {
        this.rating = rating;
    }

    public int getAsInt() {
        return this.rating;
    }
}
