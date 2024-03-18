package lt.snatovich.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EntityNotFoundException extends ResponseStatusException {
    public EntityNotFoundException(Class<?> clazz, Object id) {
        super(HttpStatus.NOT_FOUND,
                String.format("Entity '%s' with id '%s' wasn't found!", clazz.getSimpleName(), id.toString()));
    }
}
