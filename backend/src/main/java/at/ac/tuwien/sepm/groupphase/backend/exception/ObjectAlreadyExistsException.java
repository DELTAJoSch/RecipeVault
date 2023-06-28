package at.ac.tuwien.sepm.groupphase.backend.exception;

/**
 * This exception signals that a similar object already exists and this will not be created again.
 */
public class ObjectAlreadyExistsException extends Exception {
    /**
     * Create a ObjectAlreadyExistsException without any information.
     */
    public ObjectAlreadyExistsException() {

    }

    /**
     * Creates a ObjectAlreadyExistsException with the specified message.
     * This message is sent to the frontend!
     *
     * @param message The error message
     */
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Creates a ObjectAlreadyExistsException with the specified message and cause.
     * This message is sent to the frontend!
     *
     * @param message The error message
     * @param cause   An inner exception / cause
     */
    public ObjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a ObjectAlreadyExistsException with the specified exception.
     *
     * @param e An inner exception
     */
    public ObjectAlreadyExistsException(Exception e) {
        super(e);
    }
}
