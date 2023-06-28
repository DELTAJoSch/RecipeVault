package at.ac.tuwien.sepm.groupphase.backend.exception;


/**
 * Resembles an exception that occurs when an entity is not found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Default Constructor.
     */
    public NotFoundException() {
    }

    /**
     * Constructor with a message.
     *
     * @param message The message to add to the exception.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and the cause of the exception.
     *
     * @param message The message to add.
     * @param cause The cause of the exception.
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with an inner exception.
     *
     * @param e The inner exception wrapped by this not found exception.
     */
    public NotFoundException(Exception e) {
        super(e);
    }
}
