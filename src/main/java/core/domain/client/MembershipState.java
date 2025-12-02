package core.domain.client;

/**
 * Represents the state of a membership.
 * Different states determine membership validity and booking capabilities.
 */
public interface MembershipState {
    /**
     * Checks if the membership is currently active.
     *
     * @return true if active, false otherwise
     */
    boolean isActive();
    
    /**
     * Checks if the membership allows booking classes.
     *
     * @return true if booking is allowed, false otherwise
     */
    boolean canBook();
    
    /**
     * Gets a human-readable description of the membership status.
     *
     * @return the status description
     */
    String getStatusDescription();
}

