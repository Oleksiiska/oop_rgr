package core.services;

import core.domain.client.Client;
import core.domain.club.FitnessClub;
import core.domain.scheduling.GroupClass;
import core.exceptions.BookingException;
import core.exceptions.MembershipAccessException;

public class BookingService {
    
    private final MembershipService membershipService;
    
    public BookingService(MembershipService membershipService) {
        this.membershipService = membershipService;
    }
    
    public void bookClass(Client client, GroupClass groupClass, FitnessClub club) 
            throws BookingException, MembershipAccessException {
        
        // Validate membership access
        membershipService.validateClubAccess(client, club.getId());
        
        // Add participant to class
        groupClass.addParticipant(client);
    }
    
    public void cancelBooking(Client client, GroupClass groupClass) {
        groupClass.removeParticipant(client);
    }
}

