package bg.softuni.recipe.explorer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedOperation extends RuntimeException {

    public UnauthorizedOperation(String message) {
        super(message);
    }
}
