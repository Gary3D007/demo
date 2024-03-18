package lt.snatovich.demo.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
         final String message = ex.getConstraintViolations()
                .stream()
                .map((violation) ->
                        String.format("'%s' %s", violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.joining("\n"));

        return getProblemDetail(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ProblemDetail handlePropertyReferenceException(PropertyReferenceException ex) {
        return getProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ProblemDetail getProblemDetail(HttpStatus httpStatus, String message) {
        final ProblemDetail result = ProblemDetail.forStatus(httpStatus);
        result.setProperty("message", message);
        result.setProperty("timestamp", LocalDateTime.now());
        return result;
    }
}
