package at.ac.tuwien.sepm.groupphase.backend.exception;

/**
 * This exception signals that a user does not have the necessary permissions /
 * is not the owner of the object he wanted to access/change.
 */
public class UserPermissionException extends Exception {
    /**
     * Create a UserPermissionException without any information.
     */
    public UserPermissionException() {

    }

    /**
     * Creates a UserPermissionException with the specified message.
     * This message is sent to the frontend!
     *
     * @param message The error message
     */
    public UserPermissionException(String message) {
        super(message);
    }

    /**
     * Creates a UserPermissionException with the specified message and cause.
     * This message is sent to the frontend!
     *
     * @param message The error message
     * @param cause   An inner exception / cause
     */
    public UserPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a UserPermissionException with the specified exception.
     *
     * @param e An inner exception
     */
    public UserPermissionException(Exception e) {
        super(e);
    }

}
