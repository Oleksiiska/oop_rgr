package core.domain.client;

import core.domain.shop.DiscountStrategy;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {
    
    @Test
    void testMembershipBuilderWithSingleClub() {
        String clubId = "club-123";
        LocalDate startDate = LocalDate.now();
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, startDate, 500)
                .withDurationInDays(30)
                .forClub(clubId)
                .build();

        assertEquals(MembershipType.SINGLE_CLUB, membership.getType());
        assertEquals(startDate, membership.getStartDate());
        assertEquals(startDate.plusDays(30), membership.getEndDate());
        assertEquals(clubId, membership.getClubId());
        assertTrue(membership.isActive());
        assertTrue(membership.canBook());
    }

    @Test
    void testMembershipBuilderWithNetworkWide() {
        LocalDate startDate = LocalDate.now();
        Membership membership = new Membership.Builder(MembershipType.NETWORK_WIDE, startDate, 1000)
                .withDurationInDays(60)
                .build();

        assertEquals(MembershipType.NETWORK_WIDE, membership.getType());
        assertNull(membership.getClubId());
        assertTrue(membership.hasAccessToClub("any-club-id"));
    }

    @Test
    void testMembershipHasAccessToClubSingleClub() {
        String clubId = "club-123";
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                .withDurationInDays(30)
                .forClub(clubId)
                .build();

        assertTrue(membership.hasAccessToClub(clubId));
        assertFalse(membership.hasAccessToClub("other-club"));
    }

    @Test
    void testMembershipHasAccessToClubNetworkWide() {
        Membership membership = new Membership.Builder(MembershipType.NETWORK_WIDE, LocalDate.now(), 1000)
                .withDurationInDays(60)
                .build();

        assertTrue(membership.hasAccessToClub("club-1"));
        assertTrue(membership.hasAccessToClub("club-2"));
        assertTrue(membership.hasAccessToClub("any-club"));
    }

    @Test
    void testMembershipBuilderThrowsWhenTypeNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Membership.Builder(null, LocalDate.now(), 500).build());
    }

    @Test
    void testMembershipBuilderThrowsWhenStartDateNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Membership.Builder(MembershipType.SINGLE_CLUB, null, 500).build());
    }

    @Test
    void testMembershipBuilderThrowsWhenDurationInvalid() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                    .withDurationInDays(0)
                    .build());
    }

    @Test
    void testMembershipBuilderThrowsWhenSingleClubWithoutClubId() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                    .build());
    }

    @Test
    void testMembershipExpiredState() {
        Membership expiredMembership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now().minusDays(60), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        assertFalse(expiredMembership.isActive());
        assertFalse(expiredMembership.canBook());
        assertTrue(expiredMembership.getStatusDescription().contains("закінчився"));
    }

    @Test
    void testMembershipNotStartedState() {
        Membership futureMembership = new Membership.Builder(MembershipType.SINGLE_CLUB, 
                LocalDate.now().plusDays(5), 500)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        assertFalse(futureMembership.isActive());
        assertFalse(futureMembership.canBook());
    }

    @Test
    void testMembershipBuilderClearsClubIdForNetworkWide() {
        Membership membership = new Membership.Builder(MembershipType.NETWORK_WIDE, LocalDate.now(), 1000)
                .forClub("some-club")  // Should be ignored
                .build();

        assertNull(membership.getClubId());
    }

    @Test
    void testMembershipWithPercentageDiscount() {
        float originalCost = 1000.0f;
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(15);
        
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), originalCost)
                .withDurationInDays(30)
                .forClub("club-123")
                .withDiscount(discount)
                .build();

        assertEquals(originalCost, membership.getOriginalCost(), 0.01f);
        assertEquals(850.0f, membership.getCost(), 0.01f); // 1000 - 15%
        assertEquals(150.0f, membership.getDiscountAmount(), 0.01f);
        assertNotNull(membership.getDiscountStrategy());
    }

    @Test
    void testMembershipWithFixedDiscount() {
        float originalCost = 1000.0f;
        core.domain.shop.DiscountOperation discount = DiscountStrategy.fixedDiscount(100);
        
        Membership membership = new Membership.Builder(MembershipType.NETWORK_WIDE, LocalDate.now(), originalCost)
                .withDurationInDays(60)
                .withDiscount(discount)
                .build();

        assertEquals(originalCost, membership.getOriginalCost(), 0.01f);
        assertEquals(900.0f, membership.getCost(), 0.01f);
        assertEquals(100.0f, membership.getDiscountAmount(), 0.01f);
    }

    @Test
    void testMembershipWithoutDiscount() {
        float originalCost = 500.0f;
        
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), originalCost)
                .withDurationInDays(30)
                .forClub("club-123")
                .build();

        assertEquals(originalCost, membership.getOriginalCost(), 0.01f);
        assertEquals(originalCost, membership.getCost(), 0.01f); // No discount
        assertEquals(0.0f, membership.getDiscountAmount(), 0.01f);
    }

    @Test
    void testMembershipWithDiscountThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 500)
                    .forClub("club-123")
                    .withDiscount(null)
                    .build());
    }

    @Test
    void testMembershipDiscountStrategyDescription() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(20);
        Membership membership = new Membership.Builder(MembershipType.SINGLE_CLUB, LocalDate.now(), 1000)
                .forClub("club-123")
                .withDiscount(discount)
                .build();

        assertNotNull(membership.getDiscountStrategy().getDescription());
        assertTrue(membership.getDiscountStrategy().getDescription().contains("20"));
    }
}

