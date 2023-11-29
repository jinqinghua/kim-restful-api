package kim.restful.api.common.exception.handler;

import kim.restful.api.common.exception.ExceptionResponse;
import kim.restful.api.common.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionResponse> handleControllerException(Throwable throwable) {
        log.error("{}.handleControllerException()", this.getClass().getSimpleName(), throwable);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(throwable.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException exception) {
        log.warn("{}.handleAuthenticationException()", this.getClass().getSimpleName(), exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException exception) {
        log.warn("{}.handleAccessDeniedException", this.getClass().getSimpleName(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error("{}.handleResourceNotFoundException()", this.getClass().getSimpleName(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(exception.getMessage()));
    }

}
