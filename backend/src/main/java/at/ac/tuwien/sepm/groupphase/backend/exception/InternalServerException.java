package at.ac.tuwien.sepm.groupphase.backend.exception;

/**
 * This exception should be raised when an internal server error arises.
 * The message should never be sent to the frontend!
 */
public class InternalServerException extends RuntimeException {

    /**
     * Creates a new InternalServerException.
     */
    public InternalServerException() {
    }

    /**
     * Create a new internalServerException with a message.
     * This message should never be sent to the frontend!
     *
     * @param message The message to log
     */
    public InternalServerException(String message) {
        super(message);
    }

    /**
     * Create a new InternalServerException with a message and a cause.
     * The message should never be sent to the frontend!
     *
     * @param message The message to log
     * @param cause   The cause of the exception
     */
    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new InternalServerException with an internal exception.
     *
     * @param e The inner exception
     */
    public InternalServerException(Exception e) {
        super(e);
    }
}
