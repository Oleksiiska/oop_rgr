package core.domain.scheduling;

/**
 * Represents the state of a group class.
 * Different states determine what operations are allowed on the class.
 */
public interface ClassState {
    /**
     * Checks if participants can be added to the class.
     *
     * @return true if participants can be added, false otherwise
     */
    boolean canAddParticipant();
    
    /**
     * Checks if the class can be cancelled.
     *
     * @return true if the class can be cancelled, false otherwise
     */
    boolean canCancel();
    
    /**
     * Checks if the class can be started.
     *
     * @return true if the class can be started, false otherwise
     */
    boolean canStart();
    
    /**
     * Gets a human-readable description of the class status.
     *
     * @return the status description
     */
    String getStatusDescription();
}

