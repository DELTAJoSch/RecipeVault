package at.ac.tuwien.sepm.groupphase.backend.exception;

import java.util.LinkedList;
import java.util.List;

/**
 * This exception signals that a deletion failed.
 */
public class DeletionException extends Exception {
    private final List<String> deletionFailures;

    /**
     * Create a DeletionException without any information.
     */
    public DeletionException(List<String> deletionFailures) {
        this.deletionFailures = deletionFailures;
    }


    /**
     * Create a DeletionException without any information and one failure.
     */
    public DeletionException(String deletionFailure) {
        List<String> failures = new LinkedList<String>();
        failures.add(deletionFailure);
        this.deletionFailures = failures;
    }

    /**
     * Creates a DeletionException with the specified message.
     * This message is sent to the frontend!
     *
     * @param message            The error message
     * @param deletionFailures The reasons the deletion failed
     */
    public DeletionException(List<String> deletionFailures, String message) {
        super(message);
        this.deletionFailures = deletionFailures;
    }

    /**
     * Creates a DeletionException with the specified message and cause.
     * This message is sent to the frontend!
     *
     * @param message            The error message
     * @param cause              An inner exception / cause
     * @param deletionFailures The reasons the deletion failed
     */
    public DeletionException(List<String> deletionFailures, String message, Throwable cause) {
        super(message, cause);
        this.deletionFailures = deletionFailures;
    }

    /**
     * Creates a DeletionException with the specified exception.
     *
     * @param e                  An inner exception
     * @param deletionFailures The reasons the deletion failed
     */
    public DeletionException(List<String> deletionFailures, Exception e) {
        super(e);
        this.deletionFailures = deletionFailures;
    }

    /**
     * Returns a list of reasons the deletion failed.
     *
     * @return Returns a List of strings containing reasons the deletion failed
     */
    public List<String> getFailureReasons() {
        return this.deletionFailures;
    }
}


