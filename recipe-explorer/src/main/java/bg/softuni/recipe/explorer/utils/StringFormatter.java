package bg.softuni.recipe.explorer.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringFormatter {

    public static String mapConstantCaseToUpperCase(String value) {

        return Arrays.stream(value.split("_"))
                .map(String::toLowerCase)
                .map(s -> {
                    char firstChar = Character.toUpperCase(s.charAt(0));
                    return firstChar + s.substring(1);
                })
                .collect(Collectors.joining(" "));
    }
}
