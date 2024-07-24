package bg.softuni.recipe.explorer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class PersistenceException extends RuntimeException{

    public PersistenceException(String message) {
        super(message);
    }
}
