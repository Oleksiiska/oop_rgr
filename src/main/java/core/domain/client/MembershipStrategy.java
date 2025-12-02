package core.domain.client;

import core.interfaces.IMembershipStrategy;

/**
 * Strategy implementations for different membership types.
 */
public class MembershipStrategy {
    
    /**
     * Strategy for SINGLE_CLUB membership type.
     */
    public static class SingleClubStrategy implements IMembershipStrategy {
        @Override
        public boolean hasAccessToClub(String clubId, String membershipClubId) {
            return membershipClubId != null && membershipClubId.equals(clubId);
        }
    }
    
    /**
     * Strategy for NETWORK_WIDE membership type.
     */
    public static class NetworkWideStrategy implements IMembershipStrategy {
        @Override
        public boolean hasAccessToClub(String clubId, String membershipClubId) {
            // Network-wide membership has access to all clubs
            return true;
        }
    }
    
    /**
     * Gets the appropriate strategy for a membership type.
     *
     * @param type the membership type
     * @return the strategy implementation for the membership type
     */
    public static IMembershipStrategy getStrategy(MembershipType type) {
        return switch (type) {
            case SINGLE_CLUB -> new SingleClubStrategy();
            case NETWORK_WIDE -> new NetworkWideStrategy();
        };
    }
}

