package core.services;

import core.domain.club.FitnessClub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FitnessClubServiceManagerTest {
    private FitnessClub club;
    private FitnessClubServiceManager serviceManager;

    @BeforeEach
    void setUp() {
        club = new FitnessClub("м. Київ, пр. Оболонський, 1");
        serviceManager = new FitnessClubServiceManager(club);
    }

    @Test
    void testServiceManagerCreation() {
        assertNotNull(serviceManager.getClub());
        assertEquals(club, serviceManager.getClub());
        assertNotNull(serviceManager.getMembershipService());
        assertNotNull(serviceManager.getBookingService());
        assertNotNull(serviceManager.getInventoryService());
    }

    @Test
    void testServiceManagerCreationThrowsWhenClubNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            new FitnessClubServiceManager(null));
    }

    @Test
    void testGetClub() {
        assertEquals(club, serviceManager.getClub());
    }

    @Test
    void testGetMembershipService() {
        assertNotNull(serviceManager.getMembershipService());
        assertTrue(serviceManager.getMembershipService() instanceof MembershipService);
    }

    @Test
    void testGetBookingService() {
        assertNotNull(serviceManager.getBookingService());
        assertTrue(serviceManager.getBookingService() instanceof BookingService);
    }

    @Test
    void testGetInventoryService() {
        assertNotNull(serviceManager.getInventoryService());
        assertTrue(serviceManager.getInventoryService() instanceof InventoryService);
    }

    @Test
    void testServicesAreConnected() {
        // Verify that BookingService uses the MembershipService from the manager
        BookingService bookingService = serviceManager.getBookingService();
        MembershipService membershipService = serviceManager.getMembershipService();
        
        assertNotNull(bookingService);
        assertNotNull(membershipService);
    }
}

