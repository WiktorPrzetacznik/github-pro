package wip.githubpro.model.error;

/**
 * @param status  http status code associated with the error
 * @param message message associated with the error
 */
public record ErrorData(int status, String message) {
}