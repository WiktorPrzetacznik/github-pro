package wip.githubpro.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import wip.githubpro.model.error.ErrorData;

@RestControllerAdvice
public class DefaultApiExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorData> handleException(WebClientResponseException e) {
        return ResponseEntity
                .status(e.getStatusCode().value())
                .body(new ErrorData(e.getStatusCode().value(), e.getStatusText()));
    }

}