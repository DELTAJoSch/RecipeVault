package at.ac.tuwien.sepm.groupphase.backend.exception;

import java.util.LinkedList;
import java.util.List;

/**
 * This exception signals that an operation's validation failed.
 */
public class ValidationException extends Exception {
    private final List<String> validationFailures;

    /**
     * Create a ValidationException without any information.
     */
    public ValidationException(List<String> validationFailures) {
        this.validationFailures = validationFailures;
    }


    /**
     * Create a ValidationException without any information and one failiure.
     */
    public ValidationException(String validationFailure) {
        List<String> failiures = new LinkedList<String>();
        failiures.add(validationFailure);
        this.validationFailures = failiures;
    }

    /**
     * Creates a ValidationException with the specified message.
     * This message is sent to the frontend!
     *
     * @param message            The error message
     * @param validationFailures The reasons the validation failed
     */
    public ValidationException(List<String> validationFailures, String message) {
        super(message);
        this.validationFailures = validationFailures;
    }

    /**
     * Creates a ValidationException with the specified message and cause.
     * This message is sent to the frontend!
     *
     * @param message            The error message
     * @param cause              An inner exception / cause
     * @param validationFailures The reasons the validation failed
     */
    public ValidationException(List<String> validationFailures, String message, Throwable cause) {
        super(message, cause);
        this.validationFailures = validationFailures;
    }

    /**
     * Creates a ValidationException with the specified exception.
     *
     * @param e                  An inner exception
     * @param validationFailures The reasons the validation failed
     */
    public ValidationException(List<String> validationFailures, Exception e) {
        super(e);
        this.validationFailures = validationFailures;
    }

    /**
     * Returns a list of reasons the validation failed.
     *
     * @return Returns a List of strings containing reasons the validation failed
     */
    public List<String> getFailureReasons() {
        return this.validationFailures;
    }

}
