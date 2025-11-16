package core.services;

import core.domain.client.Client;
import core.domain.client.Membership;
import core.domain.staff.Administrator;
import core.exceptions.MembershipAccessException;

public class MembershipService {
    
    /*Assigns a membership to a client after administrator approval.*/
    public void assignMembership(Client client, Membership membership, Administrator administrator) {
        if (administrator == null) {
            throw new IllegalArgumentException("Адміністратор не може бути null.");
        }
        
        if (membership == null) {
            throw new IllegalArgumentException("Абонемент не може бути null.");
        }
        
        // Get administrator approval before assigning
        boolean approved = administrator.approveNewMembership(membership);
        
        if (!approved) {
            throw new IllegalArgumentException("Абонемент не було затверджено адміністратором.");
        }
        
        // Assign membership after approval
        client.assignMembership(membership, administrator);
    }
    
    /*Validates if a client can access a specific club.*/
    public void validateClubAccess(Client client, String clubId) throws MembershipAccessException {
        if (!client.hasActiveMembership()) {
            throw new MembershipAccessException("Абонемент неактивний.");
        }
        
        if (!client.getMembership().hasAccessToClub(clubId)) {
            throw new MembershipAccessException("Абонемент не дійсний для цього клубу.");
        }
    }
}

