package core.interfaces;

public interface IMembershipStrategy {
    /*Checks if membership has access to a specific club.*/
    boolean hasAccessToClub(String clubId, String membershipClubId);
}

