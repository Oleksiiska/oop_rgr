package core.services;

import core.domain.club.FitnessClub;
import core.domain.shop.Inventory;

public class FitnessClubServiceManager {
    
    private final FitnessClub club;
    private final MembershipService membershipService;
    private final BookingService bookingService;
    private final InventoryService inventoryService;
    
    public FitnessClubServiceManager(FitnessClub club) {
        if (club == null) {
            throw new IllegalArgumentException("Клуб не може бути null.");
        }
        this.club = club;
        this.membershipService = new MembershipService();
        this.bookingService = new BookingService(membershipService);
        this.inventoryService = new InventoryService(club.getInventory());
    }
    
    public FitnessClub getClub() {
        return club;
    }
    
    public MembershipService getMembershipService() {
        return membershipService;
    }
    
    public BookingService getBookingService() {
        return bookingService;
    }
    
    public InventoryService getInventoryService() {
        return inventoryService;
    }
}

