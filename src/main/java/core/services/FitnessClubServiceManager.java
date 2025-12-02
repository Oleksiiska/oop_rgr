package core.services;

import core.domain.club.FitnessClub;
import core.util.Constants;
import core.util.ValidationUtils;

/**
 * Service manager that provides access to all services for a fitness club.
 * Acts as a facade for managing club-related operations.
 */
public class FitnessClubServiceManager {
    
    private final FitnessClub club;
    private final MembershipService membershipService;
    private final BookingService bookingService;
    private final InventoryService inventoryService;
    
    /**
     * Creates a new service manager for the specified club.
     *
     * @param club the fitness club to manage (must not be null)
     * @throws IllegalArgumentException if club is null
     */
    public FitnessClubServiceManager(FitnessClub club) {
        this.club = ValidationUtils.requireNonNull(club, Constants.ERROR_CLUB_NULL);
        this.membershipService = new MembershipService();
        this.bookingService = new BookingService(membershipService);
        this.inventoryService = new InventoryService(club.getInventory());
    }
    
    /**
     * Gets the fitness club managed by this service manager.
     *
     * @return the fitness club
     */
    public FitnessClub getClub() {
        return club;
    }
    
    /**
     * Gets the membership service.
     *
     * @return the membership service
     */
    public MembershipService getMembershipService() {
        return membershipService;
    }
    
    /**
     * Gets the booking service.
     *
     * @return the booking service
     */
    public BookingService getBookingService() {
        return bookingService;
    }
    
    /**
     * Gets the inventory service.
     *
     * @return the inventory service
     */
    public InventoryService getInventoryService() {
        return inventoryService;
    }
}

