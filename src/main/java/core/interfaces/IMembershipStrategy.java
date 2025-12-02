package core.interfaces;

/**
 * Strategy interface for determining membership access to clubs.
 * Different membership types implement different access strategies.
 */
public interface IMembershipStrategy {
    /**
     * Checks if a membership grants access to a specific club.
     *
     * @param clubId the ID of the club to check access for
     * @param membershipClubId the club ID associated with the membership (may be null for network-wide memberships)
     * @return true if access is granted, false otherwise
     */
    boolean hasAccessToClub(String clubId, String membershipClubId);
}

