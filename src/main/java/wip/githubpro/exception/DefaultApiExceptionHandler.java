package wip.githubpro.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import wip.githubpro.model.error.ErrorData;

@RestControllerAdvice
public class DefaultApiExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorData> handleException(HttpClientErrorException e) {
        return ResponseEntity
                .status(e.getStatusCode().value())
                .body(new ErrorData(e.getStatusCode().value(), e.getStatusText()));
    }

}