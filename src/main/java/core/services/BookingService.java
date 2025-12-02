package core.services;

import core.domain.client.Client;
import core.domain.club.FitnessClub;
import core.domain.scheduling.GroupClass;
import core.exceptions.BookingException;
import core.exceptions.MembershipAccessException;
import core.util.ValidationUtils;
import core.util.Constants;
import org.springframework.stereotype.Service;

/**
 * Service for managing class bookings.
 * Handles booking and cancellation of group classes.
 */
@Service
public class BookingService {
    
    private final MembershipService membershipService;
    
    /**
     * Creates a new booking service with the specified membership service.
     *
     * @param membershipService the membership service for access validation (must not be null)
     * @throws IllegalArgumentException if membershipService is null
     */
    public BookingService(MembershipService membershipService) {
        this.membershipService = ValidationUtils.requireNonNull(membershipService, "MembershipService не може бути null.");
    }
    
    /**
     * Books a client for a group class.
     * Validates membership access before adding the participant.
     *
     * @param client the client to book (must not be null)
     * @param groupClass the class to book (must not be null)
     * @param club the club where the class takes place (must not be null)
     * @throws BookingException if booking fails (e.g., class full, class cancelled)
     * @throws MembershipAccessException if client does not have access to the club
     * @throws IllegalArgumentException if any parameter is null
     */
    public void bookClass(Client client, GroupClass groupClass, FitnessClub club) 
            throws BookingException, MembershipAccessException {
        
        ValidationUtils.requireNonNull(client, "Клієнт не може бути null.");
        ValidationUtils.requireNonNull(groupClass, "Заняття не може бути null.");
        ValidationUtils.requireNonNull(club, Constants.ERROR_CLUB_NULL);
        
        // Validate membership access
        membershipService.validateClubAccess(client, club.getId());
        
        // Add participant to class
        groupClass.addParticipant(client);
    }
    
    /**
     * Cancels a client's booking for a group class.
     *
     * @param client the client whose booking to cancel (must not be null)
     * @param groupClass the class to cancel booking for (must not be null)
     * @throws IllegalArgumentException if any parameter is null
     */
    public void cancelBooking(Client client, GroupClass groupClass) {
        ValidationUtils.requireNonNull(client, "Клієнт не може бути null.");
        ValidationUtils.requireNonNull(groupClass, "Заняття не може бути null.");
        groupClass.removeParticipant(client);
    }
}

