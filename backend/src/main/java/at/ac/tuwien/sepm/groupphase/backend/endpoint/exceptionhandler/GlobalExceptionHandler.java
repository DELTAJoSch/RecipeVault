package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Register all your Java exceptions here to map them into meaningful HTTP exceptions
 * If you have special cases which are only important for specific endpoints, use ResponseStatusExceptions
 * https://www.baeldung.com/exception-handling-for-rest-with-spring#responsestatusexception
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions.
     */
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handle Validation Exceptions produced by the validators.
     *
     * @param ex      The ValidationException to handle
     * @param request The request to send the error message to
     */
    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidation(ValidationException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, flattenValidationException(ex), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handle Validation Exceptions produced by the validators.
     *
     * @param ex      The ConstraintViolationException to handle
     * @param request The request to send the error message to
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidation(ConstraintViolationException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, flattenConstraintViolationException(ex), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handle Deletion Exceptions produced by service.
     *
     * @param ex      The DeletionException to handle
     * @param request The request to send the error message to
     */
    @ExceptionHandler(value = {DeletionException.class})
    protected ResponseEntity<Object> handleDeletion(DeletionException ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, flattenDeletionException(ex), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handle User Permission Exceptions produced by the validators.
     *
     * @param ex      The UserPermissionException to handle
     * @param request The request to send the error message to
     */
    @ExceptionHandler(value = {UserPermissionException.class})
    protected ResponseEntity<Object> handleUserPermission(Exception ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    /**
     * Handle Object already exists Exceptions produced by the validators.
     *
     * @param ex      The ObjectAlreadyExistsException to handle
     * @param request The request to send the error message to
     */
    @ExceptionHandler(value = {ObjectAlreadyExistsException.class})
    protected ResponseEntity<Object> handleObjectAlreadyExistsException(Exception ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Handle Internal Server Errors produced by the validators.
     *
     * @param ex      The InternalServerException to handle
     * @param request The request to send the error message to
     */
    @ExceptionHandler(value = {InternalServerException.class})
    protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        LOGGER.warn(ex.getMessage());
        LOGGER.error(Arrays.toString(ex.getStackTrace()));
        return handleExceptionInternal(ex, "Something went wrong.", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Override methods from ResponseEntityExceptionHandler to send a customized HTTP response for a known exception
     * from e.g. Spring
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        //Get all errors
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());
        body.put("Validation errors", errors);

        return new ResponseEntity<>(body.toString(), headers, status);

    }

    /**
     * Flatten a validation exception's reasons in order to display the exception better.
     *
     * @param e The validationException to flatten.
     * @return Returns the flattened string.
     */
    private String flattenValidationException(ValidationException e) {
        StringBuilder builder = new StringBuilder("Validation Failed: ");

        for (var reason : e.getFailureReasons()) {
            builder.append(reason).append("\n");
        }

        return builder.toString();
    }

    /**
     * Flatten a validation exception's reasons in order to display the exception better.
     *
     * @param e The ConstraintViolationException to flatten.
     * @return Returns the flattened string.
     */
    private String flattenConstraintViolationException(ConstraintViolationException e) {
        StringBuilder builder = new StringBuilder("Validation Failed: ");

        for (var reason : e.getConstraintViolations()) {
            builder.append(reason.getMessage()).append("\n");
        }

        return builder.toString();
    }

    private String flattenDeletionException(DeletionException e) {
        StringBuilder builder = new StringBuilder("Löschen fehlgeschlagen: ");

        for (var reason : e.getFailureReasons()) {
            builder.append(reason).append("\n");
        }

        return builder.toString();
    }
}
